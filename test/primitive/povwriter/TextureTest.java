/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primitive.povwriter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sid
 */
public class TextureTest {
    
    /**
     * 
     */
    public TextureTest() {
    }
    
    /**
     * 
     */
    @BeforeClass
    public static void setUpClass() {
    }
    
    /**
     * 
     */
    @AfterClass
    public static void tearDownClass() {
    }
    
    /**
     * 
     */
    @Before
    public void setUp() {
    }
    
    /**
     * 
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of equals method, of class Texture.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object o = new Texture(Finish.CHROME, 3400);
        Texture instance = new Texture(Finish.CHROME, 3401);;
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
     }

    /**
     * Test of hashCode method, of class Texture.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Texture instance = new Texture(Finish.CHROME, 3400);
        int expResult = 0;
        int result = instance.hashCode();
    }
}
