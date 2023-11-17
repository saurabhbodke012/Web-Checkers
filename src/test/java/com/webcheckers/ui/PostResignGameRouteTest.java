package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class PostResignGameRouteTest {
    private PostResignGameRoute CuT;
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private PlayerLobby playerLobby;
    private GameCenter gameCenter;
    private Game game;
    private Response response;
    private Gson gson = new Gson();


    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        playerLobby = mock(PlayerLobby.class);
        gameCenter = mock(GameCenter.class);
        game = mock(Game.class);

        when(request.session()).thenReturn(session);
        CuT = new PostResignGameRoute(engine, gson, playerLobby, gameCenter);
    }

    @Test
    public void inEmptyState() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(new Player("Ajay"));
        when(gameCenter.getGame("Ajay")).thenReturn(game);
        when(game.emptyState()).thenReturn(true);

        //invoke test
        String messageReceived = (String) CuT.handle(request, response);
        verify(session, times(1)).attribute(eq(PostSigninRoute.PLAYER_KEY));
        verify(gameCenter, times(1)).getGame("Ajay");
        verify(game, times(1)).emptyState();
        verify(gameCenter, times(1)).removeGameBoard("Ajay");
        verify(game, times(1)).resign("Ajay");

        //assert
        assertTrue(gson.toJson(Message.info("Resigned from game")).equals(messageReceived));
    }

    @Test
    public void notEmptyState() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(new Player("Ajay"));
        when(gameCenter.getGame("Ajay")).thenReturn(game);
        when(game.emptyState()).thenReturn(false);

        //invoke
        String messageReceived = (String) CuT.handle(request, response);
        verify(session, times(1)).attribute(eq(PostSigninRoute.PLAYER_KEY));
        verify(gameCenter, times(1)).getGame("Ajay");
        verify(game, times(1)).emptyState();

        //assert
        assertTrue(gson.toJson(Message.error("You may only resign in the empty turn state")).equals(messageReceived));
    }

}