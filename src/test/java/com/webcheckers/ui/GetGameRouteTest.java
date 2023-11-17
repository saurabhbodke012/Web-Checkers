package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import java.util.ArrayList;
import java.util.List;



/**
 * The unit test suite for the {@link GetGameRoute} component.
 *NOTE:NOT COMPLETE, there are still more branches to be tested in this class
 * @author <a href='mailto:rmj7752@rit.edu'>Randall John</a>
 */
//@Tag("UI-tier")
//public class GetGameRouteTest {
//
//    /**
//     * The component-under-test (CuT).
//     */
//    private GetGameRoute CuT;
//
//    private Request request;
//    private Session session;
//    private Response response;
//    private TemplateEngine engine;
//
//    private List<Player> players = new ArrayList<>();
//    private Player p1;
//    private Player p2;
//    private PlayerLobby lobby;
//    private Game game;
//    private GameCenter gc;
//    private Gson gson;
//
//
//    @BeforeEach
//    public void setUp() {
//        request = mock(Request.class);
//        session = mock(Session.class);
//        response = mock(Response.class);
//        engine = mock(TemplateEngine.class);
//        lobby = mock(PlayerLobby.class);
//        game = mock(Game.class);
//        gc = mock(GameCenter.class);
//        p1 = mock(Player.class);
//        p2 = mock(Player.class);
//
//        when(request.session()).thenReturn(session);
//
//        CuT = new GetGameRoute(engine,gson, lobby, gc);
//    }
//
//    /**
//     * Test that Game view will make a new game when 2nd player is not null
//     */
//    @Test
//    public void newGame() throws Exception {
//        // Arrange the test scenario: The session holds no game.
//        //when(session.attribute(GetHomeRoute.PLAYERS_LIST_ATTR)).thenReturn(players);
//
//        // To analyze what the Route created in the View-Model map you need
//        // to be able to extract the argument to the TemplateEngine.render method.
//        // Mock up the 'render' method by supplying a Mockito 'Answer' object
//        // that captures the ModelAndView data passed to the template engine
//        final TemplateEngineTester testHelper = new TemplateEngineTester();
//        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
//
//        //get second player
//        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(p2);
//        //get first player
//        when(lobby.getPlayer(any())).thenReturn(p1);
//
//        //force second player to not be in a game yet
//        when(gc.getGame(any())).thenReturn(null);
//
//
//        // Invoke the test
//        CuT.handle(request, response);
//
//
//        // Analyze the content passed into the render method
//        //   * model is a non-null Map
//        testHelper.assertViewModelExists();
//        testHelper.assertViewModelIsaMap();
//        //   * model contains all necessary View-Model data
//        testHelper.assertViewModelAttribute(GetGameRoute.TITLE_KEY, "New Game");
//        testHelper.assertViewModelAttribute(GetGameRoute.VIEW_MODE_KEY, GetGameRoute.gameMode.PLAY);
//        //   * test view name
//        testHelper.assertViewName(GetGameRoute.GAME_FTL);
//
//    }
//
//    /**
//     * Test that Game view will make a new game when 2nd player is the first one to request the game
//     */
//    @Test
//    public void newGameSecondPlayer() throws Exception {
//        // Arrange the test scenario: The session holds no game.
//        //when(session.attribute(GetHomeRoute.PLAYERS_LIST_ATTR)).thenReturn(players);
//
//        // To analyze what the Route created in the View-Model map you need
//        // to be able to extract the argument to the TemplateEngine.render method.
//        // Mock up the 'render' method by supplying a Mockito 'Answer' object
//        // that captures the ModelAndView data passed to the template engine
//        final TemplateEngineTester testHelper = new TemplateEngineTester();
//        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
//
//        //get second player
//        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(p2);
//        //get first player
//        when(lobby.getPlayer(any())).thenReturn(p1);
//        //create a mock name for 2nd player
//        when(p2.getName()).thenReturn("randall");
//        //make sure 2nd player is in a game (game not null)
//        when(gc.getGame(any())).thenReturn(game);
//        when(game.getRedPlayerName()).thenReturn("randall");
//        when(game.getWhitePlayerName()).thenReturn("john");
//
//
//        // Invoke the test
//        CuT.handle(request, response);
//
//
//        // Analyze the content passed into the render method
//        //   * model is a non-null Map
//        testHelper.assertViewModelExists();
//        testHelper.assertViewModelIsaMap();
//        //   * model contains all necessary View-Model data
//        testHelper.assertViewModelAttribute(GetGameRoute.TITLE_KEY, "New Game");
//        testHelper.assertViewModelAttribute(GetGameRoute.VIEW_MODE_KEY, GetGameRoute.gameMode.PLAY);
//        testHelper.assertViewModelAttribute(GetGameRoute.FLIPPED_KEY, false);
//        //   * test view name
//        testHelper.assertViewName(GetGameRoute.GAME_FTL);
//
//    }
//*/
//}
