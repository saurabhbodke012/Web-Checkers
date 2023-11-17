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

class PostBackupMoveRouteTest {
    private PostBackupMoveRoute CuT;

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
        CuT = new PostBackupMoveRoute(gson, gameCenter);
    }

    @Test
    public void PlayerAndBoard(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(new Player("Peter"));

        when(gameCenter.getGame("Peter")).thenReturn(game);

        // Invoke
        String messageReceived = (String) CuT.handle(request, response);

        verify(session, times(1)).attribute(eq(PostSigninRoute.PLAYER_KEY));
        verify(gameCenter, times(1)).getGame("Peter");

        verify(game, times(1)).clearMoves();

        // assert
        assertTrue(gson.toJson(Message.info("The Last move is undone for you.")).equals(messageReceived));
    }

    @Test
    public void invalidPlayer(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(null);
        when(gameCenter.getGame("Peter")).thenReturn(game);

        // Invoke
        String messageReceived = (String) CuT.handle(request, response);

        verify(session, times(1)).attribute(eq(PostSigninRoute.PLAYER_KEY));
        verify(gameCenter, times(0)).getGame("Peter");
        verify(game, times(0)).clearMoves();

        //assert
        assertNull(messageReceived);
    }

    @Test
    public void invalidGame(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(new Player("Peter"));
        when(gameCenter.getGame("Peter")).thenReturn(null);

        // Invoke
        String messageReceived = (String) CuT.handle(request, response);

        verify(session, times(1)).attribute(eq(PostSigninRoute.PLAYER_KEY));
        verify(gameCenter, times(1)).getGame("Peter");
        verify(game, times(0)).clearMoves();

        // assert
        assertNull(messageReceived);
    }

}