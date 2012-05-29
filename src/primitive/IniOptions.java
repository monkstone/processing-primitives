/**
 * *
 * The purpose of this library is to allow the export of processing sketches to
 * PovRAY Copyright (C) 2011 Martin Prout This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */
package primitive;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin Prout
 */
public class IniOptions implements Options {

    private List<String> options;
    private String name;
    private File iniFile;
    final String COMMENT_ANTIALIAS = "; Antialias=on";
    final String ANTIALIAS = "Antialias=on";
    final String COMMENT_MAXMEMORY = "; Max_Image_Buffer";
    final String MAXMEMORY = "Max_Image_Buffer_Memory=516";
    final String COMMENT_SAMPLING_METHOD = "; Sampling_Method=2";
    final String SAMPLING_METHOD = "Sampling_Method=2";
    final String OUT_TYPE = "Output_File_Type=N8";
    final String NO_DISPLAY = "Display=off";
    final String COMMENT_THRESHOLD = "; Antialias_Threshold=0.3";
    final String THRESHOLD = "Antialias_Threshold=0.3";
    
    
    /**
     * Basic constructor for IniOptions
     *
     * @param width window width
     * @param height window height
     * @param qual see above for suggested input in values
     * @param name
     */
    public IniOptions(int width, int height, Quality qual, String name) {
        this.name = name;
        iniFile = new File(name);
        File parent = iniFile.getParentFile();
        if (!parent.exists()) { // guard against no data directory
            parent.mkdir();
        }
        options = new ArrayList<String>();
        options.add(String.format("; %s", iniFile.getName()));
        options.add("\n\n");
        options.add(String.format("Input_File_Name=%s.pov", name.replace(".ini", "")));
        options.add(String.format("Output_File_Name=%s.png", name.replace(".ini", "")));
        options.add(String.format("Width=%d", width));
        options.add(String.format("Height=%d", height));
        options.add(String.format("Declare=ASPECT_RATIO=%.4f", width * 1.0f / height));
        options.add(String.format("Declare=ZDEPTH=%.4f", width * 0.288675f));
        setQuality(qual);
        this.name = name;
    }

    /**
     * @param qual PREVIEW .. MEDIUM .. HIGH .. HIGHEST .. GRAYSCALE Quality
     * enum
     */
    @Override
    public final void setQuality(Quality qual) {
        switch (qual) {

            case PREVIEW: // Render should be quick hence display off
                options.add(String.format("Quality=%d", qual.value()));
                options.add(NO_DISPLAY);
                options.add("Preview_Start_Size=32");
                options.add("Preview_End_Size=8");
                break;
            case MEDIUM: // display on
                options.add(String.format("Quality=%d", qual.value()));
                options.add("; Display=off");
                options.add(COMMENT_ANTIALIAS);
                options.add(COMMENT_SAMPLING_METHOD);
                options.add(COMMENT_THRESHOLD);
                options.add(COMMENT_MAXMEMORY);
                options.add(OUT_TYPE);
                break;
            case HIGH: // display on
                options.add(String.format("Quality=%d", qual.value()));
                options.add(ANTIALIAS);
                options.add(COMMENT_SAMPLING_METHOD);
                options.add(COMMENT_THRESHOLD);
                options.add(MAXMEMORY);
                options.add(OUT_TYPE);
                break;
            case HIGHEST: // display off
                options.add(String.format("Quality=%d", qual.value()));
                options.add(NO_DISPLAY);
                options.add(OUT_TYPE);
                options.add(MAXMEMORY);
                options.add(ANTIALIAS);
                options.add(SAMPLING_METHOD);
                options.add(THRESHOLD);
                break;
            case GRAYSCALE: // display is full color
                options.add(String.format("Quality=%d", qual.value()));
                options.add(ANTIALIAS);
                options.add(MAXMEMORY);
                options.add("Grayscale_Output=true");
                break;
            default:
                options.add(String.format("Quality=%d", 7));
                options.add(MAXMEMORY);
                options.add(OUT_TYPE);
        }
    }

    /**
     * Custom add option to ini file
     *
     * @param type
     * @param value
     */
    @Override
    public void addOption(String type, String value) {
        options.add(String.format("%s=%s", type, value));
    }

    /**
     * Custom add declare to ini file
     *
     * @param name
     * @param value
     */
    @Override
    public void addDeclare(String name, String value) {
        options.add(String.format("Declare=%s=%s", name, value));
    }

    /**
     * Public to satisfy common interface with CommandOptions
     *
     * @return option as List of String elements
     */
    @Override
    public List<String> getArgs() {
        return options;
    }

    /**
     *
     */
    @Override
    public void writeFile() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(iniFile));
            for (String option : options) {
                pw.println(option);
            }
        } catch (IOException ex) {
            Logger.getLogger(IniOptions.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pw.flush();
            pw.close();
        }
    }

    /**
     * @return option String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String option : options) {
            sb.append(option);
            sb.append('\n');
        }
        return sb.toString();
    }
}
