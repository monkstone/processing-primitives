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

package primitive.povwriter;

/**
 * Here I use Hue to avoid obvious name-space clashes with processing 
 * java.awt.Color etc. The calculated int corresponds to the int stored by
 * processing for a given hue (supports RGB and RGBF), the int is used by
 * by my PovrayColorFactory to return a Povray color (from map, or newly
 * created by factory and stored in map for future use.
 *
 * @author Martin Prout
 */
public enum Hue {
    /**
     * 
     */
    WHITE(-1),
    /**
     * 
     */
    CORNELL_RED(-7272954),
    /**
     * 
     */
    CORNELL_GREEN(-16368634),
    /**
     * 
     */
    RED(-65536),
    /**
     * 
     */
    GREEN(-16711936),
    /**
     * 
     */
    BLUE(-16776961),
    /**
     * 
     */
    CYAN(-16711681),
    /**
     * 
     */
    YELLOW(-256),
    /**
     * 
     */
    MAGENTA(-65281),
    /**
     * 
     */
    WINE_BOTTLE(-1721321370),
    /**
     * 
     */
    SILVER(-4144960),
    /**
     * 
     */
    BRASS(-11717862),
    /**
     * 
     */
    CHROME(-13421773),
    /**
     * 
     */
    COPPER(-10079450),
    /**
     * 
     */
    BUDDHA_GOLD(-4087804),
    /**
     * 
     */
    BRIGHT_GOLD(-2500327),
    /**
     * 
     */
    NEON_BLUE(-11710977),
    /**
     * 
     */
    BLACK(-16777216);
    
    private final int colour;

    Hue(int colour) {
        this.colour = colour;
    }

    /**
     * 
     * @return
     */
    public final int color() {
        return colour;
    }
}
