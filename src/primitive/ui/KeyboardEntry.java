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
import processing.core.PApplet;


/**
 * 
 * @author Martin Prout
 */
public class KeyboardEntry {

    boolean clicked = false;
    /**
     *
     */
    protected final PApplet applet;

    /**
     *
     * @param outer
     */
    public KeyboardEntry(final PApplet outer) {
        this.applet = outer;
        this.applet.registerKeyEvent(this);
    }
    
    /**
     * Get record button status
     * @return
     */
    public boolean getClicked() {
        return clicked;
    }

    
    /**
     * Key events to register, for no-gui doesn't display eg with peasycam
     *
     * @param e
     */
    public void keyEvent(KeyEvent e) {
        if (KeyEvent.KEY_RELEASED == e.getID()) {
            switch (e.getKeyChar()) {
                case 'r':
                case 'R':
                    if (clicked == false)     // idempotent
                        clicked = true;
                    break;
            }
        }
    }

    /**
     * Requirement of a processing library
     */
//    public void dispose() {
//    }
}

