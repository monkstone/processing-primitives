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
 * Using typesafe enum State to track state surprisingly enough, very linear, so 
 * no need for a fancy (or set of booleans) state-machine could be fun though
 * @author Martin Prout
 */
public enum State {
    /**
     * Initial application state
     */
    START,
    /**
     * Recording povray file
     */
    RECORDING,
    /**
     * Recording completed
     */
    RECORDED,
    /**
     * Running povray
     */
    TRACING,
    /**
     * Povray ray tracing complete
     */
    TRACED;    
}
