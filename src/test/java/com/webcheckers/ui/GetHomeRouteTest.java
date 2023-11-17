package com.webcheckers.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.model.Game;

import spark.HaltException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import java.util.ArrayList;

/**
 * The unit test suite for the {@link GetHomeRoute} component.
 *
 * @author <a href='mailto:pjw7904@rit.edu'>Peter Willis</a>
 */
@Tag("UI-tier")
public class GetHomeRouteTest {
    // The component under test
    private GetHomeRoute CuT;

    // mock objects - UI dependencies
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private Response response;
    private Player thePlayer;

    // mock objects - checkers dependencies
    private PlayerLobby lobby;
    private GameCenter gameCenter;

    /**
     * Setup new mock objects and instantiate a new CuT object for each test.
     */
    @BeforeEach
    void setUp() {
        // Set up the mock objects for UI dependencies
        request = mock(Request.class);
        session = mock(Session.class);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        when(request.session()).thenReturn(session);

        // Set up the mock objects for the game dependencies
        lobby = mock(PlayerLobby.class);
        gameCenter = mock(GameCenter.class);
        thePlayer = mock(Player.class);

        // Set up the CuT object being tested
        CuT = new GetHomeRoute(lobby, gameCenter, engine);
    }

    // BUGGY TEST
    /*
    /**
     * Test that CuT shows the Home view when the session DOES NOT have a logged-in player.
     */
    /*
    @Test
    public void logged_out_session() {
        // Render model when appropriate
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Random, valid value for number of logged-in players
        final int TEST_NUM = 3;
        when(lobby.getNumberOfActivePlayers()).thenReturn(TEST_NUM);

        // Force the player session to be null for logged-out test
        when(session.attribute(PostSigninRoute.PLAYER_KEY)).thenReturn(null);

        /* Invoke the test, no player session attribute */
        //CuT.handle(request, response);

        /* Analyze the results */
        // 1. Model is a non-null Map:
       // testHelper.assertViewModelExists();
       // testHelper.assertViewModelIsaMap();

        // 2. Model contains all necessary View-Model data for a new, logged-out player:
       // testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE_LOGGED_OUT);
       // testHelper.assertViewModelAttribute(GetHomeRoute.MSG_ATTR, GetHomeRoute.WELCOME_MSG);
       // testHelper.assertViewModelAttribute(GetHomeRoute.AVAILABLE_PLAYER_KEY, Boolean.TRUE);
       // testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_COUNT_ATTR, String.format(GetHomeRoute.NUMBER_PLAYERS_MSG, TEST_NUM));

        // 3. Test view name:
        //testHelper.assertViewName(GetHomeRoute.VIEW_NAME);



    /**
     * Test that CuT shows the Home view when the session DOES have a logged-in player, but
     * another player HAS NOT picked them to play a game of checkers (no redirect to the game page).
     */
    @Test
    public void logged_in_no_game_session() {
        // Render model when appropriate
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Force the player session to be not null for logged-in test
        when(session.attribute(PostSigninRoute.PLAYER_KEY)).thenReturn(thePlayer);

        // Force the game center to confirm the mock player is not a part of a Checkers game
        when(gameCenter.getGame(thePlayer.getName())).thenReturn(null);

        // Create a mock list of players
        ArrayList<Player> playerList = new ArrayList<>();
        when(lobby.getPlayers()).thenReturn(playerList);

        /* Invoke the test, player session attribute included/player is logged in */
        CuT.handle(request, response);

        /* Analyze the results */
        // 1. Model is a non-null Map:
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        // 2. Model contains all necessary View-Model data for a logged-in player:
        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE_LOGGED_IN);
        testHelper.assertViewModelAttribute(GetHomeRoute.MSG_ATTR, GetHomeRoute.INSTRUCTIONS_MSG);
        testHelper.assertViewModelAttribute(GetHomeRoute.USER_ATTR, thePlayer);
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYERS_LIST_ATTR, playerList);

        // 3. Test view name:
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
    }

    /**
     * Test that CuT shows the Home view when the session DOES have a logged-in player and
     * another player HAS picked them to play a game of checkers (needs to be redirected to game page).
     */
    @Test
    public void logged_in_game_redirect() {
        // Render model when appropriate
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Force the player session to be not null for logged-in test
        when(session.attribute(PostSigninRoute.PLAYER_KEY)).thenReturn(thePlayer);

        // Force the game center to confirm the mock player is going to be in a Checkers game
        when(gameCenter.getGame(thePlayer.getName())).thenReturn(mock(Game.class));

        /* Invoke the test, player logged in, but was picked to be in a Checkers game  */
        try {
            CuT.handle(request, response);
            fail("Redirects invoke halt exceptions.");
        } catch (HaltException e) {
            // expected
        }

        /* Analyze the results (should redirect to game page) */
        verify(response).redirect(WebServer.GAME_URL);
    }
}