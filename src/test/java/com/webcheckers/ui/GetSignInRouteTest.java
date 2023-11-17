package com.webcheckers.ui;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.ArrayList;

/**
 * The unit test suite for the {@link GetSigninRoute} component.
 *
 * @author Ajay Shewale
 */
@Tag("UI-tier")
public class GetSignInRouteTest {

    // private static final attributes for Player Names
    private static final String PLAYER_NAME_1 = "Naruto";
    private static final String PLAYER_NAME_2 = "Sasuke";

    /**
     * The component-under-test (CuT).
     */
    private GetSigninRoute CuT;

    // mock objects
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private PlayerLobby playerLobby;
    private Response response;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        // return the value of session when request is made
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        // create mock engine
        engine = mock(TemplateEngine.class);
        // create mock player lobby
        playerLobby = mock(PlayerLobby.class);
        // create the players arraylist
        ArrayList<Player> players = new ArrayList<>();
        // add two already signed in players to the player lobby
        players.add(new Player(PLAYER_NAME_1));
        players.add(new Player(PLAYER_NAME_2));
        when(playerLobby.getPlayers()).thenReturn(players);

        // create a unique CuT for each test
        CuT = new GetSigninRoute(engine);
    }

    @Test
    public void testSignedIn(){

        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        // If we get the player key then player is already signed in
        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(new Player(PLAYER_NAME_1));

        // Invoke
        String received = (String) CuT.handle(request, response);

        // Analyze
        // * verifying that user is redirected to the HOME_URL
        verify(response, times(1)).redirect(WebServer.HOME_URL);

        // * verify that null is returned
        assertNull(received);
    }

    @Test
    public void notSignedIn(){

        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // If we get the player key then player is already signed in
        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(null);

        // Invoke
        CuT.handle(request, response);

        // verifying that user is redirected to the HOME_URL
        verify(session, times(1)).attribute(eq(PostSigninRoute.PLAYER_KEY));

        // Analyze
        // * assert view model exists
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        // * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetSigninRoute.TITLE_KEY, GetSigninRoute.TITLE);
        testHelper.assertViewModelAttribute(GetSigninRoute.MSG_KEY, GetSigninRoute.MSG_VAL);

        // * test view name
        testHelper.assertViewName(GetSigninRoute.VIEW_NAME);

    }


}
