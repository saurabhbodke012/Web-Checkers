package com.webcheckers.application;

import com.webcheckers.appl.GameCenter;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.util.ValidationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * The unit test suite for the {@link GameCenter} component.
 *
 * @author Ajay Shewale
 * @author Randall John
 */

@Tag("Application-tier")
public class GameCenterTest {

    // private static final attributes for Player Name
    private static final String PLAYER_NAME = "Naruto";

    /**
     * The component-under-test (CuT).
     */
    private GameCenter CuT;
    private Game game;
    HashMap<String, Game> gmap;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {

        // create the GameCenter
        CuT = new GameCenter();

        // use mock function to create game mock object
        game = mock(Game.class);
        gmap = CuT.getGameMap();
    }

    /**
     * Test to construct a new GameCenter constructor.
     */
    @Test
    public void test_create_game() {
        new GameCenter();
    }

    /**
     * Test to add game in the game map
     */
    @Test
    public void test_add_game() {

        // Setup
        // Invoke: add game in game map
        CuT.addGame(PLAYER_NAME,game);

        GameCenter gc2 = new GameCenter();
        Game game2 = mock(Game.class);
        Game game3 = new Game(new Board(), "1", "2", Piece.Color.RED, new ValidationHelper());
        game3.setGameID(null);

        when(gc2.getGameFromID("")).thenReturn(null);
        gc2.addGame("Randall", game2);
        gc2.addGame("Randall2", game3);

        // Analyse that game is created
        assertEquals(1,gmap.size());
        assertEquals(game,gmap.get(PLAYER_NAME));
        assertTrue(gc2.getGameMap().containsKey("Randall"));
    }
    /**
     * Test to getGame method
     */
    @Test
    public void test_get_game() {

        // Setup
        // Invoke: add game in game map
        CuT.addGame(PLAYER_NAME,game);

        // Analyse: get game for player which is added
        assertEquals(game,CuT.getGame(PLAYER_NAME));
    }

    @Test
    public void removeGameBoard() {
        CuT.addGame(PLAYER_NAME, game);
        CuT.removeGameBoard(PLAYER_NAME);

        assertEquals(0, CuT.getGameMap().size());
    }
}
