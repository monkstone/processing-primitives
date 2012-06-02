/**
 * The purpose of this library is to allow the export of processing sketches to
 * PovRAY Copyright (C) 2012 Martin Prout This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */
package primitive;

import primitive.povwriter.Finish;
import primitive.povwriter.Texture;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * This sketch is adapted from the DXF export processing library example, and
 * demonstrates the use of povwriter library to export raw processing data to
 * povray SDL
 *
 * @author Martin Prout
 */
public class PovExporterTest extends PApplet {
    PovExporter export;
    int[] DATA = {
        -1, 0, 1
    };

    /**
     * Processing Setup
     */
    @Override
    public void setup() {
        size(1280, 1024, P3D);
        export = new PovExporter(this);
        camera(250.0f, 10.0f, 250.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        export.chooseTemplate();
        export.setPovrayPath("/usr/local/bin/povray"); //use this once to set povray path        
        export.createIniFile(dataPath("balls.ini"), Quality.MEDIUM);
        export.addDeclareOption("ScaleP5", "0.15"); // custom declare,translates Y axis in PovRAY
        export.addDeclareOption("TransYP5", "200");
        noStroke();
        sphereDetail(18);
    }

    /**
     * Processing Draw
     */
    @Override
    public void draw() {
        if (export.traced()) {
            display();
        } else {
            lights();        // this needs to be outside the record loop
            export.beginRaw(dataPath("balls.pov"));
            background(200);
            render();
            export.endRaw();  // needs to match begin raw (in nesting etc)
        }

    }

    /**
     * Display ray-traced image in sketch window
     */
    public void display() {
        PImage img = loadImage(dataPath("balls.png"));
        background(img);
    }

    /**
     * Render processing sketch
     */
    public void render() {
        for (int y : DATA) {
            for (int x : DATA) {
                for (int z : DATA) {
                    pushMatrix();
                    translate(120 * x, 120 * y, 120 * z);
                    int col = color(random(255), random(255), random(255)); // a nice test for my colorFactory class
                    if (x == 0 && y == 0) {
                        if (z == 0) {
                            export.setTexture(new Texture(Finish.MIRROR));
                            export.sphere(60);
                        } else {
                            export.setTexture(new Texture(Finish.METAL, col));
                            export.box(60);
                        }
                        
                    } else {
                        export.setTexture(new Texture(Finish.PHONG1, col));
                    }
                    export.sphere(30);
                    popMatrix();
                }
            }

        }
    }

    /**
     * Main
     *
     * @param args
     */
    public static void main(String[] args) {
        PApplet.main(new String[]{"--bgcolor=#DFDFDF", "primitive.PovExporterTest"});

    }
}
