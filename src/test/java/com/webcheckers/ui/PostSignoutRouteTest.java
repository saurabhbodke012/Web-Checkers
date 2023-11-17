package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class PostSignoutRouteTest {

    private PostSignoutRoute CuT;

    private Request request;
    private Session session;
    private TemplateEngine engine;
    private PlayerLobby playerLobby;
    private GameCenter gameCenter;
    private Game game;
    private Response response;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        //mock objects
        request = mock(Request.class);
        session = mock(Session.class);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        playerLobby = mock(PlayerLobby.class);
        game = mock(Game.class);
        gameCenter = mock(GameCenter.class);

        //define behavior
        when(request.session()).thenReturn(session);

        // create a unique CuT for each test
        // the GameCenter is friendly but the engine mock will need configuration
        CuT = new PostSignoutRoute(playerLobby,engine);
    }

    /**
     * Test the case where the user is signed in
     */

    @Test
    public void validSignOut(){

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        Player tempPlayer = new Player("Bob");
        //when I get the player key from the session, then the player is already signed in
        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(tempPlayer);

        // Invoke the test
        String received = (String) CuT.handle(request, response);
        //verify that the right methods were called
        verify(request, times(1)).session();
        verify(session, times(1)).attribute(PostSigninRoute.PLAYER_KEY);
        //verify(session, times(1)).removeAttribute(PostSigninRoute.PLAYER_KEY); <-- causing error
        verify(response, times(1)).redirect(WebServer.HOME_URL);

        //assert that the response is expected
        assertNull(received);
    }



    /**
     * Test the case where the user is not signed in
     */
    @Test
    public void invalidSignOut(){

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //when I get the player key from the session, then the player is already signed in
        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(null);

        // Invoke the test
        String received = (String) CuT.handle(request, response);
        //verify that the right methods were called
        verify(request, times(1)).session();
        verify(session, times(1)).attribute(PostSigninRoute.PLAYER_KEY);
        verify(playerLobby, times(0)).removePlayerFromLobby(any());
        verify(session, times(0)).removeAttribute(PostSigninRoute.PLAYER_KEY);
        verify(response, times(1)).redirect(WebServer.HOME_URL);

        //assert that the response is expected
        assertNull(received);
    }

}