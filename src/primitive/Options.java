/**
 * The purpose of this library is to allow the export of processing sketches to PovRAY
 * Copyright (C) 2012 Martin Prout
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation; 
 * either version 2.1 of the License, or (at your option) any later version.
 * 
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */

package primitive;

import java.util.List;

/**
 *
 * @author Martin Prout
 */
public interface Options {



    /**
     * Custom add declare to ini file
     *
     * @param name String #declare
     * @param value String 
     */
    void addDeclare(String name, String value);

    /**
     * Custom add option to ini file
     *
     * @param type option type String
     * @param value option value String
     */
    void addOption(String type, String value);

    /**
     * Get list of args
     * @return
     */
    List<String> getArgs();

    /**
     * @param qual enum
     */
    void setQuality(Quality qual);

    /**
     * Only makes sense for IniOptions currently
     */
    void writeFile();
}

