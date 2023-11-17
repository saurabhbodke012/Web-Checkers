package com.webcheckers.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the {@link Player} component.
 *
 * @author Ajay Shewale
 */

@Tag("Model-tier")
public class PlayerTest {
    // private static final attributes for Player Names
    private static final String PLAYER_NAME_1 = "Naruto";
    private static final String PLAYER_NAME_2 = "Sasuke";

    /**
     * Test to construct a new Player constructor.
     */
    @Test
    public void test_create_player() {
        new Player(PLAYER_NAME_1);
    }

    /**
     * Test to get name
     */
    @Test
    public void test_getName(){
        // Setup and Invoke
        final Player CuT = new Player(PLAYER_NAME_1);
        // Analyse
        assertEquals(PLAYER_NAME_1, CuT.getName());
    }

    /**
     * Test to equals method
     */
    @Test
    public void test_equals(){
        // Setup and Invoke
        final Player CuT1 = new Player(PLAYER_NAME_1);
        final Player CuT2 = new Player(PLAYER_NAME_1);
        final Player CuT3 = new Player(PLAYER_NAME_2);
        // Analyse
        assertEquals(CuT1, CuT1);
        assertEquals(CuT1, CuT2);
        assertNotEquals(CuT1, CuT3);
        assertNotEquals(CuT1, new Board());
    }

    /**
     * hasCode method test
     */
    @Test
    public void test_hashCode(){
        // Setup and Invoke
        final Player CuT = new Player(PLAYER_NAME_1);
        // Analyse
        assertEquals(PLAYER_NAME_1.hashCode(), CuT.hashCode());
    }

    /**
     * Test to toString method
     */
    @Test
    public void test_toString(){
        // Setup and Invoke
        final Player CuT = new Player(PLAYER_NAME_1);
        // Analyse
        assertEquals(PLAYER_NAME_1, CuT.toString());
    }
}
