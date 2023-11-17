package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

/**
 * The UI Controller to POST a check of whose turn it currently is.
 *
 * @author <a href='mailto:pjw7904@rit.edu'>Peter Willis</a>
 */
public class PostSpectatorCheckTurnRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostSpectatorCheckTurnRoute.class.getName());

    public static final Message GAME_UPDATED_MSG = Message.info("true");

    public static final String TURN_KEY = "currentTurnColor";

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;
    private final Gson gson;

    // the player associated with the session
    private Player spectator;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     * @param templateEngine the HTML template rendering engine
     */
    public PostSpectatorCheckTurnRoute(TemplateEngine templateEngine, Gson gson,  GameCenter gameCenter) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.gameCenter = Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        this.gson = Objects.requireNonNull(gson, "gson must not be null");

        LOG.config("PostSpectatorCheckTurnRoute is initialized.");
    }

    /**
     * Render the WebCheckers Game Page
     * @param request - the HTTP request
     * @param response - he HTTP response
     * @return - it returns the rendered HTML for the game page
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("PostSpectatorCheckTurnRoute is invoked.");

        final Session httpSession = request.session();

        // Grab the session player information, if valid.
        spectator = httpSession.attribute(PostSigninRoute.PLAYER_KEY);

        // If the spectator isn't logged in, push them to the home page
        if (spectator == null) {
            httpSession.attribute(GetHomeRoute.UPDATED_STATUS_MSG, Message.info("You are not logged in, you cannot spectate a game."));
            return gson.toJson(GAME_UPDATED_MSG);
        }

        // Get the game ID
        String gameID = gson.fromJson(request.queryParams("gameID"), String.class);
        Game spectatingGame = gameCenter.getGameFromID(gameID);

        // If the game cannot be found, push them to the home page
        if(spectatingGame == null) {
            httpSession.attribute(GetHomeRoute.UPDATED_STATUS_MSG, Message.info("Game attempting to be spectated cannot be found."));
            return gson.toJson(GAME_UPDATED_MSG);
        }

        Message turnResult;

        if(spectatingGame.gameOver()) {
            httpSession.attribute(GetHomeRoute.UPDATED_STATUS_MSG, Message.info(spectatingGame.getGameEndMsg()));
            turnResult = GAME_UPDATED_MSG;
        }

        else {
            Piece.Color currentTurn = httpSession.attribute(PostSpectatorCheckTurnRoute.TURN_KEY);

            if(currentTurn == spectatingGame.getActiveColor()) {
                turnResult = Message.info(String.format("It is still %s's turn", (currentTurn == Piece.Color.RED) ? spectatingGame.getRedPlayerName() : spectatingGame.getWhitePlayerName()));
            }

            else {
                turnResult = GAME_UPDATED_MSG;
                httpSession.attribute(PostSpectatorCheckTurnRoute.TURN_KEY, spectatingGame.getActiveColor());
            }
        }

        return gson.toJson(turnResult);
    }
}
