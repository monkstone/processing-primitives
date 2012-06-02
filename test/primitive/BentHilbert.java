package primitive;

import java.text.CharacterIterator;
import lsystem.Grammar;
import lsystem.SimpleGrammar;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * This sketch is the f_test for processing 1.5.1
 *
 * @author Martin Prout
 */
public class BentHilbert extends PApplet {

    PovExporter export;
    Grammar grammar;
    float distance = 100;
    int depth = 3;
    float theta = 90;
    float phi = 90;
    String production = "";

    /**
     * Processing Setup
     */
    @Override
    public void setup() {
        size(500, 500, P3D);
        camera(250.0f, 10.0f, 250.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        setupGrammar();
        noStroke();
        export = new PovExporter(this);
        export.chooseTemplate();
        export.setPovrayPath("/usr/local/bin/povray"); //use this once to set povray path
        // Quality PREVIEW, MEDIUM, HIGH, HIGHEST, GRAYSCALE
        export.createIniFile(dataPath("hilbert.ini"), Quality.MEDIUM);
        export.addDeclareOption("TransYP5", "10"); // custom declare,translates Y axis in PovRAY
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
            export.beginRaw(dataPath("hilbert.pov"));  // begin tracing
            render();
            export.endRaw();  //end tracing
        }
    }

    /**
     * 
     */
    public void setupGrammar() {
        grammar = new SimpleGrammar(this, "X");   // this only required to allow applet to call dispose()
        grammar.addRule('X', "^<XF^<XFX-F^>>XFX&F+>>XFX-F>X->");
        production = grammar.createGrammar(depth);
        distance *= Math.pow(0.5, depth);
    }

    /**
     * Display ray traced image in sketch window (as background is best)
     */
    public void display() {
        PImage img = loadImage(dataPath("hilbert.png"));
        background(img);
    }

    /**
     * 
     */
    public void render() {
        translate(-distance * 3.5f, distance * 3.5f, -distance * 3.5f);  // center the hilbert 
        fill(0, 75, 152);
        lightSpecular(204, 204, 204);
        specular(255, 255, 255);
        shininess(1.0f);
        CharacterIterator it = grammar.getIterator(production);
        for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
            switch (ch) {
                case 'F':
                    translate(0, -distance/2, 0);
                    export.box(distance / 9, distance, distance / 9);
                    translate(0, -distance/2, 0);
                    break;
                case '-':
                    export.rotateX(theta);
                    break;
                case '+':
                    export.rotateX(-theta);
                    break;
                case '>':
                    export.rotateY(theta);
                    break;
                case '<':
                    export.rotateY(-theta);
                    break;
                case '&':
                    export.rotateZ(-phi);
                    break;
                case '^':
                    export.rotateZ(phi);
                    break;
                case 'X':
                    break;
                default:
                    System.err.println("character " + ch + " not in grammar");
            }
        }
    }

    /**
     * main
     *
     * @param args
     */
    public static void main(String args[]) {
        PApplet.main(new String[]{"primitive.BentHilbert"});
    }
}
