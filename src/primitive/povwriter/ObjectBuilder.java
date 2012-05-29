/***
 * The purpose of this library is to allow the export of processing sketches to PovRAY
 * Copyright (C) 2011 Martin Prout
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation; 
 * either version 2.1 of the License, or (at your option) any later version.
 * 
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */

package primitive.povwriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class 'knows' about PovRAY syntax, and can build Strings (as mutable String
 * ie StringBuilder) to represent mesh triangles and lines from processing in PovRAY 
 * format. Uses singleton ColorFactory instance to convert/store colors in PovRAY 
 * fashion, ie as declared colors. The whole processing sketch gets wrapped in a union 
 * endProcessingScene() function adds translation/scale/rotate elements.
 * (currently these get declared in the template but could be stored parameters!!!)
 * @author Martin Prout
 */
public class ObjectBuilder implements POVInterface {

    private File tmpFile;
    private BufferedWriter out;
    private PovrayColorFactory factory;

    ObjectBuilder() {
        factory = PovrayColorFactory.getFactory();
        try {
            tmpFile = File.createTempFile("povwriter", ".pov");
            out = new BufferedWriter(new FileWriter(tmpFile));
            tmpFile.deleteOnExit();
        } catch (IOException e) {
            System.out.println("Exception is" + e);
        }
    }

    /**
     * Get the temporary file we intend to read from it
     * @return
     */
    public String getTempName() {
        return tmpFile.getAbsolutePath();
    }


    private StringBuilder startTriangle(float[][] pts) {
        StringBuilder pov_triangle = new StringBuilder(120);
        pov_triangle.append("my_triangle(");
        pov_triangle.append((pts[0][0]));
        pov_triangle.append(COMMA);
        pov_triangle.append((pts[0][1]));
        pov_triangle.append(COMMA);
        pov_triangle.append((pts[0][2]));
        pov_triangle.append(COMMA);
        pov_triangle.append((pts[1][0]));
        pov_triangle.append(COMMA);
        pov_triangle.append((pts[1][1]));
        pov_triangle.append(COMMA);
        pov_triangle.append((pts[1][2]));        
        pov_triangle.append(COMMA);
        pov_triangle.append((pts[2][0]));
        pov_triangle.append(COMMA);
        pov_triangle.append((pts[2][1]));
        pov_triangle.append(COMMA);
        pov_triangle.append((pts[2][2]));
        return pov_triangle;
    }

    /** 
     * Returns a line as a PovRAY cylinder (credit to Guillaume LaBelle)
     * @param pt1
     * @param pt2
     * @return
     */
    public StringBuilder createLine(float[] pt1, float[] pt2) {
        StringBuilder pov = new StringBuilder(40);
        startLine(pt1, pt2, pov);
        return pov.append(" )\n");
    }

    /**
     * This is used by PovWriter to start union around processing objects
     * @return UNION start String 
     */
    public StringBuilder beginUnion(){
        StringBuilder primitive = new StringBuilder(95); //89
        primitive.append(INCLUDE);
        primitive.append(UNION);        
        return primitive.append(PRIMITIVES);
    }
    
    /**
     * Helper function for createLine, that avoids creating a new StringBuilder
     * @param pt1 float[] 1st point
     * @param pt2 float[] 2nd point
     * @param pov_line StringBuilder passed by reference (not value)
     * @return 
     */

    private void startLine(float[] pt1, float[] pt2, StringBuilder pov_line) {        
        pov_line.append("my_line( ");
        pov_line.append(pt1[0]);
        pov_line.append(COMMA);
        pov_line.append((pt1[1]));
        pov_line.append(COMMA);
        pov_line.append((pt1[2]));
        pov_line.append(COMMA);
        pov_line.append(pt2[0]);
        pov_line.append(COMMA);
        pov_line.append((pt2[1]));
        pov_line.append(COMMA);
        pov_line.append((pt2[2]));        
    }
    
    private StringBuilder startSphere(float[] pt1, float size, StringBuilder sphere) {
        sphere.append("my_sphere( ");
        sphere.append(pt1[0]);
        sphere.append(COMMA);
        sphere.append((pt1[1]));
        sphere.append(COMMA);
        sphere.append((pt1[2]));
        sphere.append(COMMA);
        return sphere.append(size);
    }

    /**
     * Writes the line to the out_file
     * @param pt1
     * @param pt2
     * @throws IOException
     */
    public void writeLine(float[] pt1, float[] pt2) throws IOException {
        out.append(createLine(pt1, pt2));
    }

    /**
     * Writes the triangle to the out_file
     * @param pts
     * @param col
     * @throws IOException
     */
    public void writeTriangle(float[][] pts, int col) throws IOException {
        out.append(startTriangle(pts));
        out.append(COMMA);
        out.append(factory.addColor(col));
        out.append(")\n");
    }
    
    /**
     * Writes the sphere to the out_file
     * @param pts centre coordinates as float[]
     * @param size as float
     * @param col as int
     * @throws IOException
     */
    public void writeSphere(float[] pts, float size, int col) throws IOException {
        StringBuilder pov = new StringBuilder(40);
        out.append(startSphere(pts, size, pov));
        out.append(COMMA);
        out.append(factory.addColor(col));
        out.append(")\n");
    }


    /**
     * Writes the end of the processing sketch and flushes and closes
     * the BufferedWriter (FileWriter)
     * @throws IOException
     */
    public void endProcessingObjects() throws IOException {
        out.append(endProcessingScene());
        out.flush();
        out.close();
    }


     /**
     * Returns translate/rotate/scale of processing sketch and close of union {}
     * @return StringBuilder
     */
    public StringBuilder endProcessingScene() {
        StringBuilder povString = new StringBuilder(222);
        povString.append("// -----------------------------End of processing scene\n");
        povString.append("// -----------------------------Adjust the processing scene\n");
        povString.append("translate<TransXP5, TransYP5, TransZP5>\n");
        povString.append("rotate<RotXP5, RotYP5, RotZP5>\n");
        povString.append("scale<ScaleP5, ScaleP5, ScaleP5>\n");
        povString.append("}\n");
        return povString;
    }
}