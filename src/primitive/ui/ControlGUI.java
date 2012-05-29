/**
 * Copyright (C) 2012 Martin Prout
 * The purpose of this library is to allow the export of processing sketches to 
 * PovRAY (PovRAY SDL) 
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation; 
 * either version 2.1 of the License, or (at your option) any later version.
 * 
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */

package primitive.ui;

import processing.core.PApplet;
import processing.core.PImage;


/**
 * 
 * @author Martin Prout
 */
public class ControlGUI extends Button {

    private PImage baseImage;
    private PImage clickImage;
    private PImage currentImage;
    private boolean show = true;

    /**
     *
     * @param applet
     * @param x
     * @param y
     */
    public ControlGUI(PApplet applet, int x, int y) {
        super(applet, x, y);
        baseImage = applet.loadImage("initial.png");
        clickImage = applet.loadImage("pressed.png");
        setSize(baseImage.width, baseImage.height);
        this.currentImage = baseImage;
    }

    /**
     * Update GUI 
     */
    public void update() {
        if (pressed) {
            currentImage = clickImage;
        } else {
            currentImage = baseImage;
        }
    }


    /**
     * Get record button status
     * @return
     */
    public boolean getClicked() {
        return clicked;
    }

    /**
     * hide record button
     */
    public void hide() {
        show = false;
    }

    /**
     * show gui
     */
    public void display() {
        if (show) {
            applet.image(currentImage, x, y, currentImage.width, currentImage.height);
        }
    }

    /**
     * Dispose of images on dispose
     */
    @Override
    public void dispose() {
        baseImage = null;
        clickImage = null;
        currentImage = null;
    }
}
