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

public class PostSubmitTurnRoute implements Route {
    //Attributes
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private final TemplateEngine templateEngine;
    private final Gson gson;
    private final PlayerLobby lobby;
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /signout} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostSubmitTurnRoute(final TemplateEngine templateEngine, Gson gson, PlayerLobby lobby, GameCenter gameCenter) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(gson, "gson must not be null");
        Objects.requireNonNull(lobby, "lobby must not be null");
        Objects.requireNonNull(gameCenter, "Game center must not be null");
        this.templateEngine = templateEngine;
        this.gson = gson;
        this.lobby = lobby;
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
        LOG.info("PostValidateMoveRoute is invoked.");
        // retrieve the HTTP session
        final Session httpSession = request.session();


        Player thisPlayer = httpSession.attribute(PostSigninRoute.PLAYER_KEY);
        Game game = null;

        if (thisPlayer != null){
            game = gameCenter.getGame(thisPlayer.getName());
        }

        if (game != null) {
            if(game.getMoveSequence() != null){
                if(game.allMovesMade()){
                    game.MakeMoves();
                    return gson.toJson(Message.info("The valid move(s) was submitted."));
                }
                return gson.toJson(Message.error("You have to finish your multi-jump."));
            }else{
                game.MakeMoves();
                return gson.toJson(Message.info("The valid move(s) was submitted."));
            }
        }
        response.redirect(WebServer.HOME_URL);
        return null;

    }

}
