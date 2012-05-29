/***
 * The purpose of this library is to allow the export of processing sketches to PovRAY
 * Copyright (C) 2011 Martin Prout
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation; 
 * either version 2.1 of the License, or (at your option) any later version.
 * 
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */


package primitive;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * A filter for use with JFileChooser, selects PovRAY files/templates
 * @author Martin Prout
 */
public class PovFilter extends FileFilter {

    /**
     * Accepts directory by default and files that end with ".pov"
     * @param f File
     * @return true when accepted
     */
    
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        if (f.getName().endsWith(".pov")) {
            return true;
        } else {
            return false;
        }
    }

   /**
     * 
     * @return file description
     */
    @Override
    public String getDescription() {
        return "Povray File";
    }
}
