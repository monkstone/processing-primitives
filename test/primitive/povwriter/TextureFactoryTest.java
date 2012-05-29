/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primitive.povwriter;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author sid
 */
public class TextureFactoryTest {

    TextureFactory instance;

    /**
     * 
     */
    public TextureFactoryTest() {
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
        instance = TextureFactory.getFactory();
        instance.addTexture(new Texture(Finish.RED_MARBLE, 0));
    }

    /**
     * 
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of getFactory method, of class TextureFactory.
     */
    @Test
    public void testGetFactory() {
        System.out.println("getFactory");
        TextureFactory expResult = TextureFactory.getFactory();
        assertEquals(expResult, instance);
    }

    /**
     * Test of addTexture method, of class TextureFactory.
     */
    @Test
    public void testAddTexture() {
        System.out.println("addTexture");
        Finish finish = Finish.METAL;
        int col = 0;

        String expResult = "Texture1";
        
        StringBuilder result = instance.addTexture(new Texture(finish, col));
        assertEquals(expResult, result.toString());

    }


    /**
     * Test of buildTexture method, of class TextureFactory.
     */
    @Test
    public void testBuildTexture() {
        System.out.println("buildTexture");
        StringBuilder result = instance.buildTexture(new Texture(Finish.METAL, 0));
        System.out.println(result);

    }

    /**
     * Test of declareTextures method, of class TextureFactory.
     */
    @Test
    public void testDeclareTextures() {
        System.out.println("declareTextures");
        StringBuilder result = instance.declareTextures();
        System.out.print(result);
    }
}
