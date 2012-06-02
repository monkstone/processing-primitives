/**
 *
 * The purpose of this library is to allow the export of processing sketches to
 * PovRAY Copyright (C) 2012 Martin Prout This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */
package primitive.povwriter;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * The combination of Finish and the int matching the processing color from Hue
 * are used to create a hash that gets used by TextureFactory to return a 
 * Texture, where no Hue is supplied default White is used to create an int, 
 * which (may/may not) even get used in Texture see MIRROR, RED_MARBLE Finish.
 * Note the use of EnumSet to store Finish and hence what Finish need either
 * including/and or declaring in the povray file (prevents unnecessary including
 * and/or declaring, and multiples thereof)
 *
 * @author Martin Prout
 * @see Hue
 * @see Finish
 */
public class TextureFactory {

    private EnumSet<Finish> finishSet;
    private Set<Texture> textureSet;
    private Map<Integer, String> textureMap;

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
    public static TextureFactory getFactory() { 
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
