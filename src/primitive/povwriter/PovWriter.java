/**
 * *
 * The purpose of this library is to allow the export of processing sketches to
 * PovRAY Copyright (C) 2012 Martin Prout This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */
package primitive.povwriter;

import java.io.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * File input/output functionality here
 *
 * @author Martin Prout
 */
public class PovWriter implements POVInterface {

    private final String HOME = System.getProperty("user.home");
    private final String SEPARATOR = System.getProperty("file.separator");
    private final String POV_HOME = HOME + SEPARATOR + ".povwriter" + SEPARATOR;
    private final String PROP_FILE = POV_HOME + "povwriter.properties";
    private final String POV_FILE = POV_HOME + "original.pov";
    private Properties prop;
    private ObjectBuilder builder;
    private String templatePath;
    private PrintWriter writer;
    private Set<Integer> colors = null;
    /**
     *
     */
    public String defaultTemplate = POV_FILE;

    /**
     * Constructor for PoVWriter class Initializes object builder (String
     * building utility) Initializes colors HashSet (controls duplicates)
     * Initializes the PovRAY file writer instance LOad stored parameters from
     * properties file
     *
     * @param templatePath input template as a String
     * @param pov PrintWriter output
     */
    public PovWriter(String templatePath, PrintWriter pov) {
        builder = new ObjectBuilder();
        colors = new HashSet<Integer>();
        this.writer = pov;
        loadProperties();
    }

    /**
     * Helper class
     */
    private void loadProperties() {
        prop = new Properties();

        try {
            prop.load(new BufferedReader(new FileReader(PROP_FILE)));
            templatePath = prop.getProperty("template", defaultTemplate);

        } catch (FileNotFoundException e) {
            Logger.getLogger(PovWriter.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("You should unzip povwriter resources in " + POV_HOME);
        } catch (IOException ex) {
            Logger.getLogger(PovWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     */
    public void writeHeader() {
        File file = new File(templatePath);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                writer.append(reader.readLine());
                writer.append(NEW_LINE);
            }
            writer.flush();

        } catch (IOException ex) {
            Logger.getLogger(PovWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Finish writing processing objects to a tmp file, append color
     * declarations to the PovRAY file begin the processing sketch by starting
     * the union, reads from tmp file and writes to PovRAY file closes the
     * reader.
     */
    public void declare(StringBuilder include) {
        try {
            builder.endProcessingObjects(); // finish writing objects to tmp file
            PovrayColorFactory factory = PovrayColorFactory.getFactory();
            for (Integer col : colors) {             // initialize factory create sketch colors
                factory.addColor(col);
            }
            TextureFactory tf = TextureFactory.getFactory();
            writer.append(factory.declareColours()); // declare sketch colors as reqd
            writer.append(tf.includeFinishes());
            writer.append(tf.declareTextures());
            writer.append(beginUnion(include));
            BufferedReader reader = new BufferedReader(new FileReader(builder.getTempName()));
            while (reader.ready()) {
                writer.append(reader.readLine());
                writer.append(NEW_LINE);
            }
            writer.flush();
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(PovWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     /**
     * This is used to start union around processing objects
     * @return UNION start String 
     */
    public StringBuilder beginUnion(StringBuilder include){
        StringBuilder primitive = new StringBuilder(95); //89
        primitive.append(include);
        primitive.append(UNION);        
        return primitive.append(PRIMITIVES);
    }

    /**
     * clear the colors and close the writer on dispose
     */
    public void dispose() {
        colors.clear();
        writer.close();
    }
}
