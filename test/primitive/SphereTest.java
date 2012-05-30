/**
 * The purpose of this example is to test the export of processing sketches to
 * PovRAY Copyright (C) 2011 Martin Prout This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */
package primitive;

import processing.core.PApplet;

/**
 * Uses deprecated Sphere class, demonstrates lines as rods (for ray-tracing)
 *
 * @author Martin Prout <martin_p@lineone.net>
 */
public class SphereTest extends PApplet {

    private PovExporter export;
    private float radius = 100.0F;

    /**
     *
     */
    @Override
    public void setup() {
        size(600, 600, P3D);
        export = new PovExporter(this);
        export.setPovrayPath("/usr/local/bin/povray");
        export.chooseTemplate();
        export.createIniFile(dataPath("sphere.ini"), Quality.HIGH);
        export.addDeclareOption("ScaleP5", "0.15"); // custom declare,translates Y axis in PovRAY
        export.addDeclareOption("TransYP5", "200");
    }

    /**
     *
     */
    @Override
    public void draw() {
        if (export.traced()) { // show result in processing window
            display();
        } else {
            background(100);
            lights();        // this needs to be outside the record loop
            export.beginRaw(dataPath("sphere.pov")); // begin recording/tracing
          //  translate(width / 2, height / 2, height / 2);
            fill(255);
            export.sphere(radius);
            export.endRaw();  //end tracing 
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        PApplet.main(new String[]{"--bgcolor=#DFDFDF", "primitive.SphereTest"});
    }

    /**
     *
     */
    public void display() {
        background(loadImage(dataPath("sphere.png")));
    }
}
