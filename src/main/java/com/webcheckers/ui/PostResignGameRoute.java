package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

public class PostResignGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private final TemplateEngine templateEngine;
    private final Gson gson;
    private final PlayerLobby lobby;
    private final GameCenter gameCenter;


    public PostResignGameRoute(final TemplateEngine templateEngine, Gson gson, PlayerLobby lobby, GameCenter gameCenter) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(gson, "gson must not be null");
        Objects.requireNonNull(lobby, "lobby must not be null");
        Objects.requireNonNull(gameCenter, "Game center must not be null");
        this.templateEngine = templateEngine;
        this.gson = gson;
        this.lobby = lobby;
        this.gameCenter = gameCenter;

        LOG.config("PostResignGameRoute is initialized.");
    }

    public Object handle(Request request, Response response) {
        LOG.info("PostValidateMoveRoute is invoked.");
        // retrieve the HTTP session
        final Session httpSession = request.session();

        Player thisPlayer = httpSession.attribute(PostSigninRoute.PLAYER_KEY);
        Game game = gameCenter.getGame(thisPlayer.getName());

        if (!game.emptyState()) {
            return gson.toJson(Message.error("You may only resign in the empty turn state"));
        } else {
            String playerName = thisPlayer.getName();
            gameCenter.removeGameBoard(playerName);
            thisPlayer.setInGameStatus(false);
            game.resign(playerName);
            return gson.toJson(Message.info("Resigned from game"));
        }
    }
}
