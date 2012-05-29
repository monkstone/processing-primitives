package primitive;

import java.io.PrintWriter;
import primitive.povwriter.Finish;
import primitive.povwriter.Texture;
import primitive.povwriter.TextureFactory;

/**
 *
 * @author Martin Prout
 */
public class PrimitiveWriter {

    final String COMMA = ", ";
    private PrintWriter primitiveWriter;
    private TextureFactory tf;
    private Texture currentTexture = null;

    /**
     *
     * @param writer
     */
    public PrimitiveWriter(PrintWriter writer) {
        this.primitiveWriter = writer;
        this.tf = TextureFactory.getFactory();
        declarePrimitives();
    }

    /**
     * Width, height & depth box
     *
     * @param xx
     * @param yy
     * @param zz
     * @param width
     * @param height
     * @param depth
     * @param colour
     */
    public void writeBox(float xx, float yy, float zz, float width, float height, float depth, Texture texture) {
        currentTexture = texture;
        StringBuilder boxBuilder = new StringBuilder(150); // 125 with RGB color?
        boxBuilder.append("box{<");
        boxBuilder.append(-0.5f * width);
        boxBuilder.append(COMMA);
        boxBuilder.append(-0.5f * height);
        boxBuilder.append(COMMA);
        boxBuilder.append(-0.5f * depth);
        boxBuilder.append(">, <");
        boxBuilder.append(0.5f * width);
        boxBuilder.append(COMMA);
        boxBuilder.append(0.5f * height);
        boxBuilder.append(COMMA);
        boxBuilder.append(0.5f * depth);
        boxBuilder.append(">\n");
        boxBuilder.append("translate<");
        boxBuilder.append(1.0f * xx);
        boxBuilder.append(COMMA);
        boxBuilder.append(-1.0f * yy);
        boxBuilder.append(COMMA);
        boxBuilder.append(-1.0f * zz);
        boxBuilder.append(">\n");
        boxBuilder.append("texture{");
        boxBuilder.append(tf.addTexture(currentTexture));
        boxBuilder.append("}\n");        
        this.primitiveWriter.append(boxBuilder.append("}\n"));
    }

    /**
     *
     * Scaling of 0.2 to match the macro in my simple scene template
     *
     * @param xx
     * @param yy
     * @param zz
     * @param radius
     * @param colour
     */
    public void writeBox(float xx, float yy, float zz, float radius, Texture texture) {
        currentTexture = texture;
        writeBox(xx, yy, zz, radius, radius, radius, texture);
    }

    public void writeBox(float xx, float yy, float zz, float width, float height, float depth, int colour) {
        Finish finish = (currentTexture != null) ? currentTexture.getFinish() : Finish.DEFAULT;
        currentTexture = new Texture(finish, colour);
        writeBox(xx, yy, zz, width, height, depth, currentTexture);
    }

    /**
     *
     * @param xx
     * @param yy
     * @param zz
     * @param radius
     * @param colour
     */
    public void writeBox(float xx, float yy, float zz, float radius, int colour) {
        Finish finish = (currentTexture != null) ? currentTexture.getFinish() : Finish.DEFAULT;
        currentTexture = new Texture(finish, colour);
        writeBox(xx, yy, zz, radius, radius, radius, currentTexture);
    }

    /**
     *
     * @param xx
     * @param yy
     * @param zz
     * @param radius
     * @param texture
     */
    public void writeSphere(float xx, float yy, float zz, float radius, Texture texture) {
        currentTexture = texture;
        StringBuilder sphereBuilder = new StringBuilder(100); // 85 with RGB color?
        sphereBuilder.append("sphere{<");
        sphereBuilder.append(1.0f * xx);
        sphereBuilder.append(COMMA);
        sphereBuilder.append(-1.0f * yy);
        sphereBuilder.append(COMMA);
        sphereBuilder.append(-1.0f * zz);
        sphereBuilder.append(">, ");
        sphereBuilder.append(radius * 0.2f);
        sphereBuilder.append('\n');
        sphereBuilder.append("texture{");
        sphereBuilder.append(tf.addTexture(texture));
        sphereBuilder.append("}}\n");
        // System.out.println(sphereBuilder.length()); // check actual length
        this.primitiveWriter.append(sphereBuilder);
    }

    /**
     *
     * @param xx
     * @param yy
     * @param zz
     * @param radius
     * @param colour
     */
    public void writeSphere(float xx, float yy, float zz, float radius, int colour) {
        Finish finish = (currentTexture != null) ? currentTexture.getFinish() : Finish.PHONG1;
        currentTexture = new Texture(finish, colour);
        writeSphere(xx, yy, zz, radius, currentTexture);
    }

    /**
     *
     */
    public final void declarePrimitives() {
        this.primitiveWriter.append("#declare PRIMITIVES = union{\n");
    }

    /**
     *
     */
    public void closePrimitiveWriter() {
        this.primitiveWriter.append("}\n");
        primitiveWriter.flush();
        primitiveWriter.close();
    }
}
