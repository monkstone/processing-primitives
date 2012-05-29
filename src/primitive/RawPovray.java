/***
 * The purpose of this library is to allow the export of processing sketches to PovRAY
 * Copyright (C) 2011 Martin Prout (Based on processing.org DXF export)
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation; 
 * either version 2.1 of the License, or (at your option) any later version.
 * 
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */

package primitive;

//import processing.core.PGraphics;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import primitive.povwriter.Degenerate;
import primitive.povwriter.PovWriter;
import processing.core.PGraphics3D;


/**
 * Based largely on DXF export, from standard libraries.
 * @author Martin Prout
 */
public class RawPovray extends PGraphics3D {
    static final int FCOLOR = 0;
    static final int SCOLOR = 1;
    static final int SWEIGHT = 2;
    private File out_file;
    private String in_file;
    private PrintWriter writer;
    private int[] colour;
    private int currentLayer = 0;
    private PovWriter pov_writer;
    private Degenerate deg;


    /**
     * Default constructor, initializes an array of integer to store fill color,
     * stroke weight as an integer (@todo implement use)
     * stroke color as an integer (@todo implement use)
     */
    public RawPovray() {
        colour = new int[3];
        
    }

    /**
     * Set the template (referred to as in_file) to read in
     * @param template 
     */

   // @Override
    public void setTemplate(String template){
        in_file = template;
    }

    /**
     * Set the output path to write out
     * @param path 
     */
    @Override
    public void setPath(String path) {
        //this.path = path;
        if (path != null) {
            out_file = new File(path);
            if (!out_file.isAbsolute()) {
                out_file = null;
            }
        }
        if (out_file == null) {
            throw new RuntimeException("Povwriter requires an absolute path "
                    + "for the location of the output file.");
        }
    }

// ..............................................................
    /**
     * I'm not sure about this (PovRAY supports texture layers)
     */
    @Override
    protected void allocate() {
        /*
        for (int i = 0; i < MAX_TRI_LAYERS; i++) {
        layerList[i] = NO_LAYER;
        }
         */
        setLayer(0);
    }



    /**
     * Code from DXF export, probably irrelevant here.
     * The default is zero.
     * @return
     */
    @Override
    public boolean displayable() {
        return false;  // just in case someone wants to use this on its own
    }

    // ..............................................................
    /**
     * Start the output, by creating a writer object
     */
    @Override
    public void beginDraw() {
        // have to create file object here, because the name isn't yet
        // available in allocate()
        if (writer == null) {
            try {
                writer = new PrintWriter(new FileWriter(out_file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            writeHeader();        }
    }

    /**
     * Finish the drawing, NB: we don't close the writer here
     */
    @Override
    public void endDraw() {
        writeFooter();
        writer.flush();
    }

    // ..............................................................
    /**
     * Code from DXF export, probably irrelevant here.
     * The default is zero.
     * @param layer
     */
  //  @Override
    public void setLayer(int layer) {
        currentLayer = layer;
    }

    /**
     * Create an instance of PovWriter, and Degenerate classes, write the header which
     * is essentially the template file read from external template (or dummy if !exists).
     */
    private void writeHeader() {
        pov_writer = new PovWriter(in_file, writer);
        pov_writer.writeHeader();
        deg = new Degenerate(); // possible refinement to have adjustable limit?
    }
    /**
     * Most of the real writing to file happens here, including writing the custom color
     * declarations and the entire processing sketch as a union. We dispose of the pov_writer
     * object, which in turn closes the writer. Sends a message via console to let user know
     * we've finished export
     */

    private void writeFooter() {
        pov_writer.declare();
        pov_writer.dispose(); // NB: this where PovRAY out file gets closed
        System.out.println("(Recording Done)");
    }

    /**
     * Code from DXF export, probably irrelevant here.
     * Write a line to the pov file. Available for anyone who wants to
     * insert additional commands into the POV stream?
     * @param what
     */
 //   @Override
    public void println(String what) {
        writer.println(what);
    }

    /**
     *  Write line unless degenerate
     * @param index1
     * @param index2
     * @throws IOException  
     */
    protected void writeLine(int index1, int index2) throws IOException {
        float[][] test = {vertices[index1], vertices[index2]};
        if (deg.notDegenerateLine(test)) {
           // pov_writer.writeLine(vertices[index1], vertices[index2], colour[1]);
     //       pov_writer.writeLine(vertices[index1], vertices[index2]);
        }
    }

    /**
     *  Write triangle unless degenerate
     * @throws IOException 
     */
   protected void writeTriangle() throws IOException {
        if (deg.notDegenerate(vertices)) {
    //        pov_writer.writeTriangle(vertices, colour[0]);            
         }
        vertexCount = 0;
    }

    // ..............................................................

    /**
     * Logic as from DXF export
     * @param kind
     */
    @Override
    public void beginShape(int kind) {
        shape = kind;

        if ((shape != LINES)
                && (shape != TRIANGLES)
                && (shape != POLYGON)) {
            String err =
                    "PovrayExport can only be used with beginRaw(), "
                    + "because it only supports lines and triangles";
            throw new RuntimeException(err);
        }

        if ((shape == POLYGON) && fill) {
            throw new RuntimeException("PovrayExport only supports non-filled shapes.");
        }

        vertexCount = 0;
    }

    /**
     * Two param version of vertex (unused?)
     * @param x float
     * @param y float
     */
    @Override
    public void vertex(float x, float y) {
        vertex(x, y, 0);
    }

    /**
     * Access the processing sketch vertices, adjust y & z values to
     * match PovRAY coordinate system.
     * @param x float
     * @param y float
     * @param z float
     */
    @Override
    public void vertex(float x, float y, float z) {
   //     float vertex[] = vertices[vertexCount];

  //      vertex[X] = x;  // note: not mx, my, mz like PGraphics3
  //      vertex[Y] = y * -1;  // change to the PovRAY coordinate system
  //      vertex[Z] = z * -1;  // +ve y is up and out of screen is -z

        if (fill) {
            colour[FCOLOR] = fillColor;
        }

        if (stroke) {
            colour[SWEIGHT] = (int) strokeWeight; // @todo implement this as choice
            colour[SCOLOR] = strokeColor; // @todo implement this as choice
        }

//        if (textureImage != null) {  // for the future?
//            vertex[U] = textureU;
//            vertex[V] = textureV;
//        }
        vertexCount++;

        if ((shape == LINES) && (vertexCount == 2)) {
            try {
                writeLine(0, 1);
                vertexCount = 0;
                /*
                } else if ((shape == LINE_STRIP) && (vertexCount == 2)) {
                writeLineStrip();
                 */
            } catch (IOException ex) {
                Logger.getLogger(RawPovray.class.getName()).log(Level.SEVERE, null, ex);
            }


        } else if ((shape == TRIANGLES) && (vertexCount == 3)) {
            try {
                writeTriangle();
            } catch (IOException ex) {
                Logger.getLogger(RawPovray.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * 
     * @param x
     * @param y
     * @param z
     */
    @Override
    public void translate(float x, float y, float z){
        // @todo 
    
    }

    /**
     * Logic as from DXF export
     * @param mode
     */

    @Override
    public void endShape(int mode) {
        if (shape == POLYGON) {
            for (int i = 0; i < vertexCount - 1; i++) {
                try {
                    writeLine(i, i + 1);
                } catch (IOException ex) {
                    Logger.getLogger(RawPovray.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (mode == CLOSE) {
                try {
                    writeLine(vertexCount - 1, 0);
                } catch (IOException ex) {
                    Logger.getLogger(RawPovray.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        /*
        if ((vertexCount != 0) &&
        ((shape != LINE_STRIP) && (vertexCount != 1))) {
        System.err.println("Extra vertex boogers found.");
        }
         */
    }

}
