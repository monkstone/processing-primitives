/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primitive.povwriter;

/**
 *
 * @author sid
 */
public class Texture {

    private Finish finish;
    private int colour;

    /**
     * 
     * @param finish
     * @param colour
     */
    public Texture(Finish finish, int colour) {
        this.colour = colour;
        this.finish = finish;
    }

    /**
     * 
     * @param finish
     * @param colour
     */
    public Texture(Finish finish, Colour colour) {
        this(finish, colour.color());
    }

    /**
     * 
     * @param finish
     */
    public Texture(Finish finish) {
        this(finish, Colour.WHITE.color()); //default white color
    }

    /**
     * 
     * @return
     */
    public int getColor() {
        return colour;
    }

    /**
     * 
     * @return
     */
    public Finish getFinish() {
        return finish;
    }

    @Override
    public boolean equals(Object o) {
        boolean result;
        result = (o.getClass() != Texture.class) ? false
                : (o.hashCode() == this.hashCode()) ? true : false;
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (this.finish != null ? this.finish.hashCode() : 0);
        hash = 17 * hash + this.colour;
        return hash;
    }
}
