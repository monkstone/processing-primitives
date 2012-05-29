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

import primitive.povwriter.Colour;
import primitive.povwriter.Finish;
import primitive.povwriter.Texture;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * This sketch is the f_test for processing 1.5.1
 *
 * @author Martin Prout
 */
public class FTest extends PApplet {

    PovExporter export;
 //   ArcBall arcball;

    /**
     * Processing Setup
     */
    @Override
    public void setup() {
        size(500, 500, OPENGL);
        camera(250.0f, 10.0f, 250.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        export = new PovExporter(this);
    //    arcball = new ArcBall(this, 0, 0, 0);
     //   arcball.constrain(Constrain.YAXIS);
        export.chooseTemplate();
        export.setPovrayPath("/usr/local/bin/povray"); //use this once to set povray path
        // Quality PREVIEW, MEDIUM, HIGH, HIGHEST, GRAYSCALE
        export.createIniFile(dataPath("ftest.ini"), Quality.HIGH);
        export.addDeclareOption("ScaleP5", "0.18"); // custom declare,translates Y axis in PovRAY
        export.addDeclareOption("TransYP5", "10");
    }

    /**
     * Processing Draw
     */
    @Override
    public void draw() {
        if (export.traced()) { // show result in processing window
            display();
        } else {
            background(200);
            lights();        // this needs to be outside the record loop
            export.beginRaw(dataPath("ftest.pov"));  // begin tracing
        //    translate(width / 2f, height * 0.7f, width / 20f);
          //  arcball.update();
            render();
            export.endRaw();  //end tracing
        }        
    }

    /**
     * Display ray traced image in sketch window (as background is best)
     */
    public void display() {
        PImage img = loadImage(dataPath("ftest.png"));
        background(img);
    }

    /**
     * 
     */
    public void render() {  // encapsulate the processing sketch as a function        
        export.setTexture(new Texture(Finish.CHROME));
        export.box(50);
        translate(0, -50, 0);
        export.setTexture(new Texture(Finish.PHONG0, Colour.RED));
        export.box(50);
        translate(50, 0, 0);
        export.box(50);        
        translate(-50, -50, 0);
        export.box(50);
        translate(0, -50, 0);
        export.box(50);
        translate(50, 0, 0);
        export.setTexture(new Texture(Finish.MIRROR));        
        export.box(50);
    }

    /**
     * main
     *
     * @param args
     */
    public static void main(String args[]) {
        PApplet.main(new String[]{"primitive.FTest"});
    }
}