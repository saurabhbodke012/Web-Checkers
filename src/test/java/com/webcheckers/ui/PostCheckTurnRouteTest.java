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

class PostCheckTurnRouteTest {
    private PostCheckTurnRoute CuT;

    private Request request;
    private Session session;
    private TemplateEngine engine;
    private PlayerLobby playerLobby;
    private GameCenter gameCenter;
    private Game game;
    private Response response;
    private Gson gson = new Gson();

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
        CuT = new PostCheckTurnRoute(gson, gameCenter);
    }

    @Test
    public void gameIsOver() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(new Player("Saurabh"));
        when(gameCenter.getGame("Saurabh")).thenReturn(game);
        when(game.gameOver()).thenReturn(true);

        // Invoke
        String messageReceived = (String) CuT.handle(request, response);
        verify(session, times(1)).attribute(eq(PostSigninRoute.PLAYER_KEY));
        verify(gameCenter, times(1)).getGame("Saurabh");
        verify(game, times(1)).gameOver();

        // assert
        assertTrue(gson.toJson(Message.info("true")).equals(messageReceived));
    }

    @Test
    public void isMyTurn() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        Player tempPlayer = new Player("Saurabh");
        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(tempPlayer);
        when(gameCenter.getGame("Saurabh")).thenReturn(game);
        when(game.gameOver()).thenReturn(false);
        when(game.amIActive(tempPlayer)).thenReturn(true);

        // Invoke
        String messageReceived = (String) CuT.handle(request, response);
        verify(session, times(1)).attribute(eq(PostSigninRoute.PLAYER_KEY));
        verify(gameCenter, times(1)).getGame("Saurabh");
        verify(game, times(1)).gameOver();
        verify(game, times(1)).amIActive(tempPlayer);

        // assert
        assertFalse(gson.toJson(Message.info("Saurabh")).equals(messageReceived));
    }

    @Test
    public void NotMyTurn() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        Player tempPlayer = new Player("Saurabh");
        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(tempPlayer);
        when(gameCenter.getGame("Saurabh")).thenReturn(game);
        when(game.gameOver()).thenReturn(false);
        when(game.amIActive(tempPlayer)).thenReturn(false);

        // Invoke
        String messageReceived = (String) CuT.handle(request, response);
        verify(session, times(1)).attribute(eq(PostSigninRoute.PLAYER_KEY));
        verify(gameCenter, times(1)).getGame("Saurabh");
        verify(game, times(1)).gameOver();
        verify(game, times(1)).amIActive(tempPlayer);

        // assert
        assertTrue(gson.toJson(Message.error("It is not your turn!")).equals(messageReceived));
    }


}