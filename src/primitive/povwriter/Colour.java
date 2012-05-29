package primitive.povwriter;

/**
 * 
 * @author sid
 */
public enum Colour {
    /**
     * 
     */
    WHITE(-1),
    /**
     * 
     */
    CORNELL_RED(-7272954),
    /**
     * 
     */
    CORNELL_GREEN(-16368634),
    /**
     * 
     */
    RED(-65536),
    /**
     * 
     */
    GREEN(-16711936),
    /**
     * 
     */
    BLUE(-16776961),
    /**
     * 
     */
    CYAN(-16711681),
    /**
     * 
     */
    YELLOW(-256),
    /**
     * 
     */
    MAGENTA(-65281),
    /**
     * 
     */
    WINE_BOTTLE(-1721321370),
    /**
     * 
     */
    SILVER(-4144960),
    /**
     * 
     */
    BRASS(-11717862),
    /**
     * 
     */
    CHROME(-13421773),
    /**
     * 
     */
    COPPER(-10079450),
    /**
     * 
     */
    BUDDHA_GOLD(-4087804),
    /**
     * 
     */
    BRIGHT_GOLD(-2500327),
    /**
     * 
     */
    NEON_BLUE(-11710977),
    /**
     * 
     */
    BLACK(-16777216);
    
    private final int colour;

    Colour(int colour) {
        this.colour = colour;
    }

    /**
     * 
     * @return
     */
    public final int color() {
        return colour;
    }
}
