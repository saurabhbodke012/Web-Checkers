package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

import com.webcheckers.util.Message;

/**
 * The UI Controller to POST a potential name to the sign-in page
 *
 * @author <a href='mailto:rmj7752@rit.edu'>Randall John</a>
 */
public class PostSignoutRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostSignoutRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final PlayerLobby lobby;

    private static final Message SIGNED_OUT_MSG = Message.info("You're Signed Out");

    /**
     * Create the Spark Route (UI controller) to handle sign out {@code POST /} HTTP request.
     *
     * @param templateEngine the template engine
     * @param lobby the player lobby
     *   the HTML template rendering engine
     */
    public PostSignoutRoute(final PlayerLobby lobby, TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        Objects.requireNonNull(lobby, "lobby must not be null");

        this.lobby = lobby;

        LOG.config("GetSignoutRoute is initialized.");
    }

    /**
     * Sign the player out
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.fine("PostSignoutRoute is invoked.");

        Map<String, Object> vm = new HashMap<>();
        //grab current session
        final Session currentSession = request.session();
        ModelAndView mv;

        //grab the player info from the session
        Player currentPlayer = currentSession.attribute(PostSigninRoute.PLAYER_KEY);

        //remove player from list of active players
        if (lobby.getPlayers().contains(currentPlayer)) {
            lobby.removePlayerFromLobby(currentPlayer);

            //remove the current player attribute from the session
            currentSession.removeAttribute(PostSigninRoute.PLAYER_KEY);
        }

        //redirect to home page w/ message
        response.redirect(WebServer.HOME_URL);
        return null;
    }
}