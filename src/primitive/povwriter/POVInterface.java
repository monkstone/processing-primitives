/***
 * The purpose of this library is to allow the export of processing sketches to PovRAY
 * Copyright (C) 2012 Martin Prout
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation; 
 * either version 2.1 of the License, or (at your option) any later version.
 * 
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */


package primitive.povwriter;

/**
 * Fragments of Strings used in PovRAY files
 * @author Martin Prout
 */
public interface POVInterface {
    /**
     * assign char
     */
    static final char ASSIGN = '=';
    /**
     * close curl char
     */
    static final char CLOSE_CURLY = '}';
    /**
     * a new line character
     */
    static final char NEW_LINE = '\n';
    /**
     * a single space character
     */
    static final char SPC = ' ';
    /**
     * format used in vertices/color in PovRAY
     */
    static final String COMMA = ", ";
    /**
     * format used between vertices in PovRAY
     */
    static final String CLOSE_OPEN = ">, <";   //
    /**
     *
     */
    static final String DECLARE = "#declare";
    /**
     *
     */
    static final String FINISH2 = "Finish2";
    /**
     *
     */
    static final String FINISH = "finish{ ";
    /**
     *
     */
    static final String UNION = "union{\n";
    
    /**
     * 
     */
    static final String INCLUDE = "#include \"/home/sid/NetBeansProjects/processing-primitives/data/primitives.inc\"\n\n";
    
    /**
     * 
     */
    static final String PRIMITIVES = "PRIMITIVES\n";
    
    /**
     * pigment names to be defined in template
     */
    static final String PIGMENT = "pigment{ ";
    /**
     *  color/colour/coluer
     */
    static final String COLOR = "colour ";
    /**
     *  prefix used by color factory for transparent colors
     */
    static final String PRE_COL = "Colour";
    /**
     * prefix used by color factory for transparent colors
     */
    static final String PRE_COL_T = "ColourT";
    /**
     * texture names to be defined in template
     */
    static final String TEXTURE = "texture{ ";
    /**
     * A default texture for line
     */
    static final String TEXTURE0 = "Texture0";
    /**
     * equivalent of stroke weight for povray line
     */
    static final String SWIDTH = "SWIDTH";
}
