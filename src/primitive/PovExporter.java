/**
 *
 * The purpose of this library is to allow the export of processing sketches to
 * PovRAY Copyright (C) 2012 Martin Prout This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */
package primitive;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import primitive.povwriter.Texture;
import primitive.ui.ControlGUI;
import processing.core.PApplet;
import processing.core.PConstants;

/**
 * This is the setup class for RawPovray that does the real work, revised
 * version briefly flirted with command line options for their versatility, but
 * I now very much favour use of an ini file...
 *
 * @author Martin Prout
 */
public class PovExporter {

    private PApplet parent;
    private final String VERSION = "0.80";
    /**
     * Interface to RawPovray as a String
     */
   // public static final String POV = "primitive.RawPovray";
    private Options options;
    private Properties prop;
    private final String HOME = System.getProperty("user.home");
    private final String SEPARATOR = System.getProperty("file.separator");
    private final String POV_HOME = HOME + SEPARATOR + ".povwriter" + SEPARATOR;
    private final String PROP_FILE = POV_HOME + "povwriter.properties";
    private String povrayTemplate = POV_HOME + "original.pov";
    private String povrayPath;
    private String storedTemplate;
    private String iniFile;
    private volatile String firstRun = "false";
    private State state = State.START;
    private ControlGUI gui;
    private PrimitiveWriter primitiveWriter;
    /**
     * This is the external povray process
     */
    public Process povray = null;
    private boolean notwritten = true;
    private Texture currentTexture = null;

    /**
     * PovExporter constructor to satisfy processing library requirement
     *
     * @param parent PApplet
     */
    public PovExporter(PApplet parent) {
        this.parent = parent;
        this.parent.registerDispose(this);
        this.gui = new ControlGUI(parent, 10, 10);
        this.parent.registerDraw(this);
        this.parent.registerPre(this);
        this.primitiveWriter = new PrimitiveWriter(parent.createWriter(parent.dataPath("primitives.inc")));
        prop = new Properties();
        loadProperties();
        System.out.println("Info: primitive writer version " + version());
        System.out.println("Info: built for processing-1.5.1");
    }

    /**
     *
     * @param state
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     *
     * @param state
     * @return
     */
    public boolean getState(State state) {
        return (this.state == state);
    }

    /**
     *
     * @param texture
     * @return
     */
    public void setTexture(Texture texture) {
        this.currentTexture = texture;
        parent.fill(texture.getColor());
        parent.ambient(50);
    }

    /**
     * Use to set scalar values (or with ingenuity switches) in pov file
     *
     * @param name that is used in pov file eg SizeP5
     * @param value scalar value as a string
     */
    public void addDeclareOption(String name, String value) {
        options.addDeclare(name, value);
    }

    /**
     * Scale processing sketch elements within PovRAY scene
     *
     * @param scale scalar value as a float (at suitable precision)
     */
    public void scalePovray(float scale) {
        options.addDeclare("ScaleP5", String.format("%.4f", scale));
    }

    /**
     * Set Line Width
     *
     * @param width float (avoid spurious precision)
     */
    public void lineWidth(float width) {
        options.addDeclare("SWIDTH", String.format("%.2f", width));
    }

    /**
     * Overloaded function to deal with in input Set Line Width
     *
     * @param width int (avoid spurious precision)
     */
    public void lineWidth(int width) {
        options.addDeclare("SWIDTH", String.format("%d", width));
    }

    /**
     * Rotate processing sketch elements within PovRAY scene (Y)
     *
     * @param rot degrees around Y axis
     */
    public void rotatePovray(int rot) {
        options.addDeclare("RotYP5", String.format("%d", rot));
    }

    /**
     * Use this to set PovRAY options that haven't been set eg display gamma
     *
     * @param name
     * @param value
     */
    public void addOption(String name, String value) {
        options.addOption(name, value);
    }

    /**
     * Use this to set the stored povray path
     *
     * @param povrayPath String path to povray on local system
     */
    public void setPovrayPath(String povrayPath) {
        prop.setProperty("povrayPath", povrayPath);
    }

    /**
     * Launches a JFileChooser instance allows you to choose an alternative
     * PovRAY template file for current and future sketches
     */
    public void chooseTemplate() {
        if (parent.isVisible()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    final JFileChooser fc = new JFileChooser(POV_HOME);
                    fc.setFileView(new TemplateFileView());
                    fc.addChoosableFileFilter(new PovFilter());
                    int returnVal = fc.showDialog(new JDialog(), "Choose Template");
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File pov = fc.getSelectedFile();
                        // prop.setProperty("template", pov.getAbsolutePath());
                        try {
                            setTemplateFile(pov);
                        } catch (IOException ex) {
                            Logger.getLogger(PovExporter.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
        }
    }

    /**
     * Use this in your sketch to get the stored povray path
     *
     * @return path String to povray executable on local system
     */
    public String getPovrayPath() {
        return povrayPath;
    }

    /**
     * Call external process PovRAY return proc
     *
     * @return proc Process
     */
    public Process rayTrace() {

        if (iniFile != null) {
            String[] args = new String[]{povrayPath, iniFile};
            povray = PApplet.exec(args);
        }
        return povray;
    }

    /**
     * Provides callback for template FileChooser, stores choice for future
     *
     * @param pov template File
     * @throws IOException
     */
    protected final void setTemplateFile(File pov) throws IOException {
        prop.setProperty("template", pov.getAbsolutePath());
        storeSettingsImpl();
    }

    /**
     * load stored properties from file, or default values. The coded defaults
     * for povrayPath would suit a linux user with povray 3.7 beta
     */
    private void loadProperties() {
        try {
            prop.load(new BufferedReader(new FileReader(PROP_FILE)));
            storedTemplate = prop.getProperty("template", povrayTemplate);
            povrayPath = prop.getProperty("povrayPath", "/usr/local/bin/povray");
            firstRun = prop.getProperty("first.run", "true");
            if (firstRun.equals("false")) {
                povrayTemplate = storedTemplate; // used stored value rather than the default
            }
        } catch (FileNotFoundException e) {
            System.err.println("Warning: you should unzip template.zip in " + HOME);
        } catch (IOException ex) {
            Logger.getLogger(PovExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void storeSettingsImpl() throws IOException {
        String comments = "Povray Properties";
        if (firstRun.equals("true")) {
            prop.setProperty("first.run", "false");
            prop.setProperty("template", povrayTemplate); // store default in properties
        }
        prop.store(new PrintWriter(new FileWriter(PROP_FILE)), comments);
    }

    /**
     * Current settings are stored to the properties file, since PovWriter/etc
     * use the stored settings when available, this function should always be
     * called, if you want to change runtime settings (not just to store
     * settings).
     */
    public void storeSettings() {
        //  storeSettingsImpl();
    }

    /**
     * The ini file stores the PovRAY raytrace options
     *
     * @param name path of ini file
     * @param quality
     */
    public void createIniFile(String name, Quality quality) {
        iniFile = name;
        options = new IniOptions(parent.width, parent.height, quality, name);
    }

    /**
     * The ini file stores the PovRAY raytrace options With a default of quality
     * medium
     *
     * @param dataPath
     *
     */
    public void createIniFile(String dataPath) {
        createIniFile(dataPath, Quality.MEDIUM);
    }

    /**
     * The ini file is a small file that sets the quality of ray-tracing, but
     * you can use custom declares to set scaling, translations etc.
     */
    public void writeIniFile() {
        options.writeFile();
    }

    /**
     *
     * @param radius
     */
    public void sphere(float radius) {
        if (State.START == state) {
            parent.sphere(radius);
        }
        if (State.RECORDING == state) {
            if (currentTexture != null) {
                this.primitiveWriter.writeSphere(parent.modelX(0, 0, 0), parent.modelY(0, 0, 0), parent.modelZ(0, 0, 0), radius, currentTexture);
            } else {
                this.primitiveWriter.writeSphere(parent.modelX(0, 0, 0), parent.modelY(0, 0, 0), parent.modelZ(0, 0, 0), radius, parent.g.fillColor);
            }

        }
    }

    /**
     *
     * @param radius
     */
    public void box(float radius) {
        if (State.START == state) {
            parent.box(radius);
        }
        if (State.RECORDING == state) {
            if (currentTexture != null) {
                this.primitiveWriter.writeBox(parent.modelX(0, 0, 0), parent.modelY(0, 0, 0), parent.modelZ(0, 0, 0), radius, currentTexture);
            } else {
                this.primitiveWriter.writeBox(parent.modelX(0, 0, 0), parent.modelY(0, 0, 0), parent.modelZ(0, 0, 0), radius, parent.g.fillColor);
            }

        }
    }

    /**
     *
     * @param width
     * @param height
     * @param depth
     */
    public void box(float width, float height, float depth) {
        if (State.START == state) {
            parent.box(width, height, depth);
        }
        if (State.RECORDING == state) {
            if (currentTexture != null) {
                this.primitiveWriter.writeBox(parent.modelX(0, 0, 0), parent.modelY(0, 0, 0), parent.modelZ(0, 0, 0), width, height, depth, currentTexture);
            } else {
                this.primitiveWriter.writeBox(parent.modelX(0, 0, 0), parent.modelY(0, 0, 0), parent.modelZ(0, 0, 0), width, height, depth, parent.g.fillColor);
            }

        }
    }

    /**
     * Update GUI and update State from START to RECORDING
     */
    public void update() {
        if (this.getState(State.START) && gui.getClicked()) {
            this.setState(State.RECORDING);
        }
        this.gui.update();
    }

    /**
     * beginRaw and endRaw should wrap what you want to ray-trace
     *
     * @param filename
     */
    public void beginRaw(String filename) {
        parent.pushMatrix(); // needed so we can display button in regular place
        if (getState(State.RECORDING)) {
            System.out.println(State.RECORDING);
            parent.noLights();    // let PovRAY do the lighting
            parent.beginRaw("primitive.RawPovray", filename);
        }
    }

    /**
     * beginRaw and endRaw should wrap what you want to ray-trace
     */
    public void endRaw() {
        if (this.getState(State.RECORDING)) {
            this.primitiveWriter.closePrimitiveWriter();
            this.setState(State.RECORDED);
            System.out.println(State.RECORDED);
            parent.endRaw();
        }
        if ((this.getState(State.RECORDED)) && (povray == null)) {
            povray = this.rayTrace();
            if (povray != null) {
                System.out.println(State.TRACING);
                this.setState(State.TRACING);
            }
        }
        try {
            if ((this.getState(State.TRACING)) && povray.waitFor() == 0) {
                this.setState(State.TRACED);      // set State as TRACED when povray has finished
                System.out.println(State.TRACED);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(PovExporter.class.getName()).log(Level.SEVERE, null, ex);
        } // following matches previous pushMatrix() needed to place button
        parent.popMatrix();
    }

    /**
     * Check whether we've got to TRACED State
     *
     * @return
     */
    public boolean traced() {
        return getState(State.TRACED);
    }

    /**
     * Needed to show button, called in sketch at end of draw loop see draw
     */
    public void displayRecordButton() {
        if (!this.getState(State.TRACED)) {
            parent.hint(PConstants.DISABLE_DEPTH_TEST);
            this.gui.display();
            parent.hint(PConstants.ENABLE_DEPTH_TEST);
        }
    }

    /**
     * Reduce amount of boilerplate code by two lines, ensure setting are stored
     * before sketch displayed, boolean guards against repeated saving?
     */
    public void pre() {
        if (notwritten) {
            this.storeSettings();
            this.writeIniFile();
            notwritten = false;
        }
    }

    /**
     * Processing libraries require this, doesn't do much here
     */
    public final void dispose() {
        gui.dispose();
        povray.destroy();
    }

    /**
     * Register this with PApplet to get drawn on sketch
     */
    public void draw() {
        update();
        displayRecordButton();
    }

    /**
     * Returns library version no, processing libraries require this
     *
     * @return
     */
    public final String version() {
        return VERSION;
    }
}
