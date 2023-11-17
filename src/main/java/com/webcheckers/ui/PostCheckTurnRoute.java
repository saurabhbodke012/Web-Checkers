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

import static spark.Spark.halt;

public class PostCheckTurnRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private final Gson gson;
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /signout} HTTP requests.
     */
    public PostCheckTurnRoute( Gson gson, GameCenter gameCenter) {
        Objects.requireNonNull(gson, "gson must not be null");
        Objects.requireNonNull(gameCenter, "Game center must not be null");
        this.gson = gson;
        this.gameCenter = gameCenter;

        LOG.config("PostCheckTurnRoute is initialized.");
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
        LOG.info("PostCheckTurnRoute is invoked.");
        // retrieve the HTTP session
        final Session httpSession = request.session();

        Player thisPlayer = httpSession.attribute(PostSigninRoute.PLAYER_KEY);
        Game game = gameCenter.getGame(thisPlayer.getName());

        if (game != null) {
            if (game.gameOver()) {
                return gson.toJson(Message.info("true"));
            }
            if (game.amIActive(thisPlayer)){
                return gson.toJson(Message.info("true"));
            }else{
                return gson.toJson(Message.error("It is not your turn!"));
            }
        }
        response.redirect(WebServer.HOME_URL);
        halt();
        return null;

    }

}
