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
package primitive.povwriter;

/**
 * 
 * @author sid
 */
public enum Finish {
    /**
     * 
     */
    PHONG0("finish { phong albedo 0.9 phong_size 40 }"),
    /**
     * 
     */
    PHONG1("finish{emission 0.5 phong 0.5 phong_size 10.0}"),
    /**
     * 
     */
    METAL("finish {\n\tphong 0.9\n\tphong_size 60\n\tmetallic}"),
    /**
     * 
     */
    CHROME ("finish{ F_MetalE }"),
    /**
     * 
     */
    GLASS("finish{ F_Glass5 }"),
    /**
     * 
     */
    MIRROR("finish{ reflection 1}"),
    /**
     * 
     */
    RED_MARBLE("texture{ T_Grnt28 }"),
    /**
     * 
     */
    DEFAULT("finish{emission 0.5 phong 0.5 phong_size 10.0}");

    Finish(String finish) {
        this.fin = finish;
    }
    private final String fin;

    /**
     * 
     * @return
     */
    public final String finish() {
        return this.fin;
    }
}
