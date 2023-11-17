package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Move;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class PostSubmitTurnRouteTest {
    private PostSubmitTurnRoute CuT;
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
        CuT = new PostSubmitTurnRoute(engine, gson, playerLobby, gameCenter);
    }


    @Test
    public void validSingleMove(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(new Player("Randall"));
        when(gameCenter.getGame("Randall")).thenReturn(game);
        when(game.getMoveSequence()).thenReturn(null);

        // Invoke
        String messageReceived = (String) CuT.handle(request, response);
        verify(session, times(1)).attribute(eq(PostSigninRoute.PLAYER_KEY));
        verify(gameCenter, times(1)).getGame("Randall");
        verify(game, times(1)).getMoveSequence();
        verify(game, times(0)).allMovesMade();
        verify(game, times(1)).MakeMoves();


        //assert
        assertTrue(gson.toJson(Message.info("The valid move(s) was submitted.")).equals(messageReceived));
    }

    @Test
    public void validDoubleMove(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(new Player("Randall"));
        when(gameCenter.getGame("Randall")).thenReturn(game);
        when(game.getMoveSequence()).thenReturn(new ArrayList<Move>(Arrays.asList(new Move(new Position(5, 4), new Position(4, 3)))));
        when(game.allMovesMade()).thenReturn(true);

        // Invoke
        String messageReceived = (String) CuT.handle(request, response);
        verify(session, times(1)).attribute(eq(PostSigninRoute.PLAYER_KEY));
        verify(gameCenter, times(1)).getGame("Randall");
        verify(game, times(1)).getMoveSequence();
        verify(game, times(1)).allMovesMade();
        verify(game, times(1)).MakeMoves();

        //assert
        assertTrue(gson.toJson(Message.info("The valid move(s) was submitted.")).equals(messageReceived));
    }


    @Test
    public void finishDoubleMove(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(new Player("Randall"));
        when(gameCenter.getGame("Randall")).thenReturn(game);
        when(game.getMoveSequence()).thenReturn(new ArrayList<Move>(Arrays.asList(new Move(new Position(5, 4), new Position(4, 3)))));
        when(game.allMovesMade()).thenReturn(false);

        // Invoke
        String messageReceived = (String) CuT.handle(request, response);
        verify(session, times(1)).attribute(eq(PostSigninRoute.PLAYER_KEY));
        verify(gameCenter, times(1)).getGame("Randall");
        verify(game, times(1)).getMoveSequence();
        verify(game, times(1)).allMovesMade();
        verify(game, times(0)).MakeMoves();

        //assert
        assertTrue(gson.toJson(Message.error("You have to finish your multi-jump.")).equals(messageReceived));
    }


    @Test
    public void notSignedIn(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(null);
        when(gameCenter.getGame("Randall")).thenReturn(game);

        // Invoke
        String messageReceived = (String) CuT.handle(request, response);
        verify(session, times(1)).attribute(eq(PostSigninRoute.PLAYER_KEY));
        verify(gameCenter, times(0)).getGame("Randall");
        verify(game, times(0)).getMoveSequence();
        verify(game, times(0)).allMovesMade();
        verify(game, times(0)).MakeMoves();

        //assert
        assertNull(messageReceived);
    }

    @Test
    public void noGame(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute(eq(PostSigninRoute.PLAYER_KEY))).thenReturn(new Player("Randall"));
        when(gameCenter.getGame("Randall")).thenReturn(null);

        // Invoke
        String messageReceived = (String) CuT.handle(request, response);
        verify(session, times(1)).attribute(eq(PostSigninRoute.PLAYER_KEY));
        verify(gameCenter, times(1)).getGame("Randall");
        verify(game, times(0)).getMoveSequence();
        verify(game, times(0)).allMovesMade();
        verify(game, times(0)).MakeMoves();

        //assert
        assertNull(messageReceived);

    }

}