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
 * @author <a href='mailto:pjw7904@rit.edu'>Peter Willis</a>
 */
public class PostSigninRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostSigninRoute.class.getName());

    private static final Message INVALID_NAME_MSG = Message.error("Invalid character or length inputted, please try again");
    private static final Message TAKEN_NAME_MSG = Message.error("Name already taken, please try again");

    static final String NAME_PARAM = "requestedPlayerName";
    public static final String PLAYER_KEY = "myPlayer";

    private final TemplateEngine templateEngine;
    private final PlayerLobby lobby;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostSigninRoute(PlayerLobby lobby, TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.lobby = Objects.requireNonNull(lobby, "lobby must not be null");
        LOG.config("PostSignInRoute is initialized.");
    }

    /**
     * Render the WebCheckers Home page.
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     * @return
     *   the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostSigninRoute is invoked.");

        final Session httpSession = request.session();

        // Create the hashmap
        Map<String, Object> vm = new HashMap<>();
        ModelAndView mv;

        // Put initial title
        vm.put(GetHomeRoute.TITLE_ATTR, GetSigninRoute.TITLE);

        //retrieve the username parameter
        final String candidateName = request.queryParams(NAME_PARAM);

        // Check if the name is valid or not
        if(lobby.nameIsInvalid(candidateName)) {
            mv = failedNameReservation(vm, INVALID_NAME_MSG);
        }

        else if(lobby.nameIsTaken(candidateName)) {
            mv = failedNameReservation(vm, TAKEN_NAME_MSG);
        }

        else {
            // Formally define a new player and add them to the lobby
            Player newPlayer = new Player(candidateName);
            lobby.addPlayerToLobby(newPlayer);

            httpSession.attribute(PLAYER_KEY, newPlayer);
            response.redirect(WebServer.HOME_URL);
            return null;
        }

        // render the view
        return templateEngine.render(mv);

    }

    // Private methods

    private ModelAndView failedNameReservation(final Map<String, Object> vm, Message message) {
        vm.put(GetHomeRoute.MSG_ATTR, message);
        return new ModelAndView(vm, GetSigninRoute.VIEW_NAME);
    }
}