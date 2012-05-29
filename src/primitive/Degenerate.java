/*
 * The purpose of this library is to allow the export of processing sketches to PovRAY
 * Copyright (C) 2011 Martin Prout
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation; 
 * either version 2.1 of the License, or (at your option) any later version.
 * 
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * 

 */
package primitive.povwriter;

/**
 * @author Martin Prout (Spring 2011)
 * Provides methods that take vertices input and determines whether, line/
 * triangle is degenerate (according somewhat arbitary limit definition)
 */
public class Degenerate {

    final int X = 0; // Corresponds to PGraphics constants
    final int Y = 1; // or more correctly processing PConstants
    final int Z = 2;
    final int A = 0; // Corresponds to three triangle points
    final int B = 1;
    final int C = 2;
    /*
     * limit is the minimum area to be  a valid triangle
     * We first calculated the vectors v, w (A-B), (A-C)
     * The we calculate the cross product, finally we calculate
     * magnitude (squared) * 4
     */
    private float limita;

    /**
     * 0.1f empirically determined as area not considered degenerate by Povray 3.7
     */
    public Degenerate() {
        this.limita = 0.1f;
    }

    /**
     * @param limita float area not considered degenerate
     */
    public Degenerate(float limita) {
        this.limita = limita;
    }

    /**
     * Helper function
     * Is degenerate 3 dimensional triangle
     * @param x0 ... float three dimensional triangle coordinates
     * @param y0 
     * @param z0
     * @param y1
     * @param x1
     * @param x2
     * @param z1
     * @param y2
     * @param z2
     * @return boolean true if degenerate
     */
    private boolean isDegenerate(float x0, float y0, float z0, float x1, float y1, float z1, float x2, float y2, float z2) {
        float vx, vy, vz; // vector v
        float wx, wy, wz; // vector w
        float c0, c1, c2; // cross product
        vx = x1 - x0;
        wx = x2 - x0;
        vy = y1 - y0;
        wy = y2 - y0;
        vz = z1 - z0;
        wz = z2 - z0;
        c0 = (wy * vz) - (vy * wz);// The square of the magnitude of the cross product
        c1 = (vx * wz) - (vy * vz);// vector is compared with a limit value to
        c2 = (wx * vy) - (vx * wy);// determine whether the triangle is degenerate
        float area = (c0 * c0) + (c1 * c1) + (c2 * c2);
        return area < 4.0f * limita;
    }

    /**
     * Not degenerate 3 dimensional triangle
     * @param coords float[3][3], three dimensional triangle coordinates
     * @return boolean true if degenerate
     */
    public boolean notDegenerate(float[][] coords) {
        return !isDegenerate(coords[A][X], coords[A][Y], coords[A][Z], coords[B][X], coords[B][Y], coords[B][Z], coords[C][X], coords[C][Y], coords[C][Z]);
    }

    /**
     * Not degenerate 3 dimensional line
     * @param coords float[2][3], three dimensional line coordinates
     * @return boolean true if not degenerate only, false is the most expensive
     */
    public boolean notDegenerateLine(float[][] coords) {
        return (coords[A][X] != coords[B][X]) ? true : (coords[A][Y] != coords[B][Y]) ? true : (coords[A][Z] != coords[B][Z]);
    }
}
