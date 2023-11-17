package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.Objects;
import java.util.logging.Logger;

public class PostBackupMoveRoute implements Route {
    //Attributes
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private final Gson gson;
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /signout} HTTP requests.
     *
     */
    public PostBackupMoveRoute( Gson gson, GameCenter gameCenter) {
        // validation
        Objects.requireNonNull(gson, "gson must not be null");
        Objects.requireNonNull(gameCenter, "GameCenter must not be null");
        //
        this.gson = gson;
        this.gameCenter = gameCenter;

        LOG.config("PostValidateMoveRoute is initialized.");
    }

    /**
     * Render the WebCheckers Game page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for to validate move page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.info("PostBackupMoveRoute is invoked.");
        // retrieve the HTTP session
        final Session httpSession = request.session();

        Player thisPlayer = httpSession.attribute(PostSigninRoute.PLAYER_KEY);
        Game game = null;

        if (thisPlayer != null){
            game = gameCenter.getGame(thisPlayer.getName());
        }

        if (game != null) {
            game.clearMoves();
            return gson.toJson(Message.info("The Last move is undone for you."));
        }
        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
