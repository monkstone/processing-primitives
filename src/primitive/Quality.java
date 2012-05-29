/**
 * The purpose of this library is to allow the export of processing sketches to PovRAY
 * Copyright (C) 2012 Martin Prout
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation; 
 * either version 2.1 of the License, or (at your option) any later version.
 * 
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */
package primitive;

/**
 * 
 * @author Martin Prout
 */

public enum Quality {

    /**
     * Preview quality, no display window
     */
    PREVIEW(3, "Preview"),
    /**
     * Medium quality, display window
     */
    MEDIUM(6, "Medium"),
    /**
     * High quality, display window
     */
    HIGH(9, "High"),
    /**
     * Highest quality, no display window
     */
    HIGHEST(11, "Highest"),
    /**
     * High quality Grey-scale output, coloured display window
     */
    GRAYSCALE(11, "Grayscale");
    private final int qty;
    private final String description;
    Quality(int qty, String description) {
        this.qty = qty;
        this.description = description;
    }

    /**
     * Quality Value 0 .. 11 understood by povray
     *
     * @return qty int
     */
    public int value() {
        return (int) this.qty;
    }
    /**
     * Quality Value 0 .. 11 understood by povray
     *
     * @return description int
     */
    public String description() {
        return this.description;
    }
};

