package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

/**
 * The UI Controller to GET the Game page from the perspective of a spectator
 *
 * @author <a href='mailto:pjw7904@rit.edu'>Peter Willis</a>
 */
public class GetSpectatorStopWatchingRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetSpectatorStopWatchingRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final PlayerLobby lobby;
    private final GameCenter gameCenter;
    private final Gson gson;

    // the player associated with the session
    private Player spectator;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     * @param templateEngine the HTML template rendering engine
     */
    public GetSpectatorStopWatchingRoute(TemplateEngine templateEngine, Gson gson, PlayerLobby lobby, GameCenter gameCenter) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.lobby = Objects.requireNonNull(lobby, "lobby must not be null");
        this.gameCenter = Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        this.gson = Objects.requireNonNull(gson, "gson must not be null");

        LOG.config("GetSpectatorStopWatchingRoute is initialized.");
    }

    /**
     * Render the WebCheckers Game Page
     * @param request - the HTTP request
     * @param response - he HTTP response
     * @return - it returns the rendered HTML for the game page
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("GetSpectatorStopWatchingRoute is invoked.");

        final Session httpSession = request.session();

        // Remove the associated attribute.
        httpSession.removeAttribute(PostSpectatorCheckTurnRoute.TURN_KEY);

        response.redirect(WebServer.HOME_URL);
        halt();
        return null;
    }
}
