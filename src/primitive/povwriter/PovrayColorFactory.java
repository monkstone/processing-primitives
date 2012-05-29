/**
 * *
 * The purpose of this library is to allow the export of processing sketches to
 * PovRAY Copyright (C) 2011 Martin Prout This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */
package primitive.povwriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * The main function of color factory is to translate sketch colors to povray
 * format. Pre-loads colorMap with some standard colors otherwise new color are
 * automatically given a new name. Makes use of bit shifting, and PovRAY ability
 * to accept scalar color transforms to convert between the color formats.
 *
 * @author Martin Prout
 */
public class PovrayColorFactory implements POVInterface {

    int count = -1;
    int tCount = -1;
    private HashMap<Integer, String> colourMap;
    private List<Integer> declareList;
    static PovrayColorFactory factory = new PovrayColorFactory();

    /**
     * Private constructor, for singleton, initialize collections, and add
     * default colors
     */
    private PovrayColorFactory() {
        this.colourMap = new HashMap<Integer, String>();
        this.declareList = new ArrayList<Integer>();
        initializeColors();
        initializeDeclares();
    }

    /**
     * 
     */
    public final void initializeColors() {
        this.colourMap.put(Colour.WHITE.color(), "White");
        this.colourMap.put(Colour.BLACK.color(), "Black");
        this.colourMap.put(Colour.BLUE.color(), "Blue");
        this.colourMap.put(Colour.RED.color(), "Red");
        this.colourMap.put(Colour.GREEN.color(), "Green");
        this.colourMap.put(Colour.YELLOW.color(), "Yellow");
        this.colourMap.put(Colour.COPPER.color(), "CopperMetal");
        this.colourMap.put(Colour.BRASS.color(), "BrassMetal");
        this.colourMap.put(Colour.CHROME.color(), "ChromeMetal");
        this.colourMap.put(Colour.BUDDHA_GOLD.color(), "BuddhaGold");
        this.colourMap.put(Colour.BRIGHT_GOLD.color(), "BrightGold");
        this.colourMap.put(Colour.SILVER.color(), "Silver");
        this.colourMap.put(Colour.CYAN.color(), "Cyan");
        this.colourMap.put(Colour.MAGENTA.color(), "Magenta");
        this.colourMap.put(Colour.CORNELL_RED.color(), "CornellRed");
        this.colourMap.put(Colour.CORNELL_RED.color(), "CornellGreen");
        this.colourMap.put(Colour.SILVER.color(), "Silver");
        this.colourMap.put(-6452, "LineFill");
        this.colourMap.put(-11585249, "LineCol");
        this.colourMap.put(-1275074868, "TransFill");
    }

    /**
     * 
     */
    public final void initializeDeclares() {
        this.declareList.add(Colour.COPPER.color());
        this.declareList.add(Colour.BRASS.color());
        this.declareList.add(Colour.CHROME.color());
        this.declareList.add(Colour.BUDDHA_GOLD.color());
        this.declareList.add(Colour.BRIGHT_GOLD.color());
    }

    /**
     * Supports Initialization on Demand Holder Idiom
     */
    private static class FactoryHolder {

        private static final PovrayColorFactory INSTANCE = new PovrayColorFactory();
    }

    /**
     * Supports Initialization on Demand Holder Idiom
     *
     * @return singleton instance of PovrayColorFactory
     */
    public static PovrayColorFactory getFactory() { // to declare getFactory makes no sense
        return FactoryHolder.INSTANCE;
    }

    /**
     *
     * @param col integer
     * @return formatted StringBuilder
     */
    public StringBuilder addColor(int col) {
        StringBuilder colName;
        if (colourMap.containsKey(col)) {
            colName = new StringBuilder(colourMap.get(col));
        } else {
            declareList.add(col);           // NB: already guarded against duplicates
            if ((col >> 24 & 0xFF) < 255) { // then color has transparency
                tCount++;
                colName = new StringBuilder(PRE_COL_T);
                colName.append(tCount);
                colourMap.put(col, colName.toString());
            } else {                        // then color has no transparency
                count++;
                colName = new StringBuilder(PRE_COL);
                colName.append(count);
                colourMap.put(col, colName.toString());
            }
        }
        return colName;
    }

    /**
     * Once colors are loaded this is used to append color declarations
     *
     * @return povray color declarations
     */
    public StringBuilder declareColours() {
        StringBuilder builder = new StringBuilder("// --------------Begin declare colours from sketch\n");
        builder.append("// If empty then sketch colors already colored\n");
        Iterator<Integer> iterator = declareList.iterator();
        while (iterator.hasNext()) {
            String tmp;
            Integer povCol = iterator.next();
            builder.append(DECLARE);
            builder.append(SPC);
            tmp = colourMap.get(povCol);
            builder.append(tmp);
            builder.append(SPC);
            builder.append(ASSIGN);
            int red = povCol >> 16 & 0xFF;  // efficiently get red
            int green = povCol >> 8 & 0xFF; // efficiently get green
            int blue = povCol & 0xFF;       // efficiently get blue
            if (tmp.startsWith(PRE_COL_T)) {
                builder.append(" color rgbf< ");
                builder.append(red);
                builder.append(COMMA);
                builder.append(green);
                builder.append(COMMA);
                builder.append(blue);
                builder.append(COMMA);
                int alpha = povCol >> 24 & 0xFF; // efficiently get alpha
                builder.append(alpha);
            } else {
                builder.append(" color rgb< ");
                builder.append(red);
                builder.append(COMMA);
                builder.append(green);
                builder.append(COMMA);
                builder.append(blue);
            }
            builder.append(" >/255;\n");  // NB: povray does the RGB calculation
        }
        builder.append("// --------------end of declare colours for sketch\n\n\n");
        builder.append("// --------------processing sketch begins here\n\n\n");
        return builder;
    }
}