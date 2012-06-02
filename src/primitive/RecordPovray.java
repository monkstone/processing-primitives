/**
 * *
 * The purpose of this library is to allow the export of processing sketches to
 * PovRAY Copyright (C) 2011 Martin Prout (Based on processing.org DXF export)
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */
package primitive;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import primitive.povwriter.PovWriter;

/**
 * Based largely on DXF export, from standard libraries.
 *
 * @author Martin Prout
 */
public class RecordPovray {

    private File out_file;
    private StringBuilder include_file;
    private String in_file;
    private PrintWriter writer;
    private PovWriter pov_writer;

    /**
     * Default constructor, initializes an array of integer to store fill color,
     * stroke weight as an integer (
     *
     * @todo implement use) stroke color as an integer (
     * @todo implement use)
     */
    public RecordPovray(String path) {
        //this.path = path;
        if (path != null) {
            out_file = new File(path);
            include_file = includeStringBuffer(out_file.getParent());
            if (!out_file.isAbsolute()) {
                out_file = null;
            }
            if (out_file == null) {
                throw new RuntimeException("Povwriter requires an absolute path "
                        + "for the location of the output file.");
            }
        }
    }

    private StringBuilder includeStringBuffer(String parentFile) {
        StringBuilder include = new StringBuilder(80);
        include.append("#include \"");
        include.append(parentFile);
        return include.append("/primitives.inc\"\n");
    }

    /**
     * Set the template (referred to as in_file) to read in
     *
     * @param template
     */
    // 
    public void setTemplate(String template) {
        in_file = template;
    }

    // ..............................................................
    /**
     * Start the output, by creating a writer object
     */
    public void beginDraw() {
        // have to create file object here, because the name isn't yet
        // available in allocate()
        if (writer == null) {
            try {
                writer = new PrintWriter(new FileWriter(out_file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            writeHeader();
        }
    }

    /**
     * Finish the drawing, NB: we don't close the writer here
     */
    public void endDraw() {
        writeFooter();
        writer.flush();
    }

    /**
     * Create an instance of PovWriter, and Degenerate classes, write the header
     * which is essentially the template file read from external template (or
     * dummy if !exists).
     */
    private void writeHeader() {
        pov_writer = new PovWriter(in_file, writer);
        pov_writer.writeHeader();
    }

    /**
     * Most of the real writing to file happens here, including writing the
     * custom color declarations and the entire processing sketch as a union. We
     * dispose of the pov_writer object, which in turn closes the writer. Sends
     * a message via console to let user know we've finished export
     */
    private void writeFooter() {
        pov_writer.declare(include_file);
        pov_writer.dispose(); // NB: this where PovRAY out file gets closed
    }
}
