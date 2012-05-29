/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primitive.povwriter;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author sid
 */
public class TextureFactory {

    private EnumSet<Finish> finishSet;
    private Set<Texture> textureSet;
    private Map<Integer, String> textureMap;
    static TextureFactory factory = new TextureFactory();

    private TextureFactory() {
        this.textureSet = new HashSet<Texture>();
        this.textureMap = new TreeMap<Integer, String>();        
        this.finishSet = EnumSet.of(Finish.MIRROR);
    }

    private static class FactoryHolder {
        private static final TextureFactory INSTANCE = new TextureFactory();
    }

    /**
     * 
     * @return
     */
    public static TextureFactory getFactory() { // to declare getFactory makes no sense
        return TextureFactory.FactoryHolder.INSTANCE;
    }

    /**
     * 
     * @param texture
     * @return
     */
    public StringBuilder addTexture(Texture texture) {
        StringBuilder builder = new StringBuilder(100);
        PovrayColorFactory cf = PovrayColorFactory.getFactory();
        Finish finish = texture.getFinish();
        finishSet.add(finish);
        if (this.textureSet.add(texture)) {
            String name = String.format("Texture%d", this.textureSet.size() - 1);
            cf.addColor(texture.getColor()); //safety 1st if color is new
            builder.append(name);
            textureMap.put(texture.hashCode(), name);
        } else {
            builder.append(textureMap.get(texture.hashCode()));
        }
        return builder;
    }

    /**
     * 
     * @param text
     * @return
     */
    public StringBuilder buildTexture(Texture text) {
        StringBuilder build = new StringBuilder(150);
        if (text.getFinish() == Finish.RED_MARBLE) {
            build.append(Finish.RED_MARBLE.finish());
        } else {
            PovrayColorFactory cf = PovrayColorFactory.getFactory();
            StringBuilder colour;
            colour = cf.addColor(text.getColor());
            String fin = text.getFinish().finish();
            build.append("texture{ pigment{");
            build.append(colour);
            build.append("} ");
            build.append(fin);
            build.append("} ");
        }
        return build;
    }

    /**
     * 
     * @return
     */
    public StringBuilder includeFinishes() {
        StringBuilder include = new StringBuilder(1000);
        for (Finish finish : finishSet) {
            switch (finish) {
                case RED_MARBLE:
                    include.append("#include \"stones1.inc\"\n");
                    break;
                case METAL:
                case CHROME:
                    include.append("#include \"metals.inc\"\n");
                    break;
                case GLASS:
                    include.append("#include \"glass.inc\"\n");
                    break;
                default:
                    break;
            }

        }
        return include;
    }

    /**
     * 
     * @return
     */
    public StringBuilder declareTextures() {
        StringBuilder declare = new StringBuilder(1000);
        for (Texture texture : this.textureSet) {
            String name = textureMap.get(texture.hashCode());
            declare.append("#declare ");
            declare.append(name);
            declare.append(" = ");
            declare.append(buildTexture(texture));
            declare.append("\n");
        }
        return declare;
    }
}
