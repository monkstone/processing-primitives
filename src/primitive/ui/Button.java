/** 
 * Copyright (C) 2012 Martin Prout
 * The purpose of this library is to allow the export of processing sketches to 
 * PovRAY (PovRAY SDL) 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */
package primitive.ui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import processing.core.PApplet;


/**
 * 
 * @author Martin Prout
 */
public class Button {

    int x;
    int y;
    int w;
    int h;
    boolean pressed = false;
    boolean clicked = false;
    /**
     *
     */
    protected final PApplet applet;

    /**
     *
     * @param outer
     * @param x coordinate 1st point
     * @param y coordinate 2nd point
     */
    public Button(final PApplet outer, int x, int y) {
        this.applet = outer;
        this.applet.registerDispose(this);
        this.applet.registerMouseEvent(this);
        this.applet.registerKeyEvent(this);
        this.x = x;
        this.y = y;
    }

    /**
     * Set width and height of selectable rectangle
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        this.w = width;
        this.h = height;
    }

    /**
     * determine whether point(mx, my) is over rectangle
     * @param mx 
     * @param my 
     * @return
     */
    public boolean overRect(int mx, int my) {
        if (mx >= this.x && my >= this.y && mx <= this.x + this.w && my <= this.y + this.h) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Mouse events to register
     * @param e
     */
    public void mouseEvent(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        switch (e.getID()) {
            case MouseEvent.MOUSE_PRESSED:
                pressed = overRect(mx, my); // only controls graphic
                break;
            case MouseEvent.MOUSE_CLICKED:
                if (clicked == false)     // idempotent
                    clicked = overRect(mx, my);
                break;
        }
    }
    
        /**
     * Key events to register, in case GUI doesn't display eg with peasycam
     *
     * @param e
     */
    public void keyEvent(KeyEvent e) {
        if (KeyEvent.KEY_RELEASED == e.getID()) {
            switch (e.getKeyChar()) {
                case 'r':
                case 'R':
                    if (clicked == false)     // idempotent
                        pressed = true;
                        clicked = true;
                    break;
            }
        }
    }

    /**
     * Requirement of a processing library
     */
    public void dispose() {
    }
}