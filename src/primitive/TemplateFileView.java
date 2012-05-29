/*
 * Code based on oracle FileChooser Demo.
 */
package primitive;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;
import java.io.File;
import javax.swing.Icon;
import javax.swing.filechooser.FileView;

/**
 * 
 * @author Martin Prout
 */
public class TemplateFileView extends FileView {

    /**
     * 
     */
    public Icon povrayIcon = new TemplateFileView.DiamondIcon(Color.RED);

//    @Override
//    public String getName(File f) {
//        return null;
//    }
//
//    @Override
//    public String getDescription(File f) {
//        return null;
//    }
//
//    @Override
//    public Boolean isTraversable(File f) {
//        return null;
//    }

    @Override
    public String getTypeDescription(File f) {
        String name = f.getName();
        String type = null;
        if (name != null) {
            if (name.endsWith(".pov") || name.endsWith(".POV")) {
                type = "POV Template";
            }
        }
        return type;
    }

    @Override
    public Icon getIcon(File f) {
        Icon icon = null;
        String name = f.getName();
        if (name != null) {
            if (name.endsWith(".pov") || name.endsWith(".POV")) {
                icon = povrayIcon;
            }
        }
        return icon;
    }

    class DiamondIcon implements Icon {

        private Color color;
        private boolean selected;
        private int width;
        private int height;
        private Polygon poly;
        private static final int DEFAULT_WIDTH = 10;
        private static final int DEFAULT_HEIGHT = 10;

        public DiamondIcon(Color color) {
            this(color, true, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }

        public DiamondIcon(Color color, boolean selected) {
            this(color, selected, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }

        public DiamondIcon(Color color, boolean selected, int width, int height) {
            this.color = color;
            this.selected = selected;
            this.width = width;
            this.height = height;
            initPolygon();
        }

        private void initPolygon() {
            poly = new Polygon();
            int halfWidth = width / 2;
            int halfHeight = height / 2;
            poly.addPoint(0, halfHeight);
            poly.addPoint(halfWidth, 0);
            poly.addPoint(width, halfHeight);
            poly.addPoint(halfWidth, height);
        }

        @Override
        public int getIconHeight() {
            return height;
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.translate(x, y);
            if (selected) {
                g.fillPolygon(poly);
            } else {
                g.drawPolygon(poly);
            }
            g.translate(-x, -y);
        }
    }
}
