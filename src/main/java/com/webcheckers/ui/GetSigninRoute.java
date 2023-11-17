package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.*;

import com.webcheckers.util.Message;

/**
 * The UI Controller to GET the sign-in page.
 *
 * @author <a href='mailto:pjw7904@rit.edu'>Peter Willis</a>
 */
public class GetSigninRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetSigninRoute.class.getName());
    static final String VIEW_NAME = "signin.ftl";
    static final String TITLE_KEY = "title";
    static final String TITLE = "Sign-in";
    static final String MSG_KEY = "message";
    static final Message MSG_VAL = Message.info("Enter your name to sign in");
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetSigninRoute(TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("GetHomeRoute is initialized.");
    }

    /**
     * Render the WebCheckers Home page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     * @return
     *   the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetSigninRoute is invoked.");

        final Session httpSession = request.session();

        //if the player is signed in, show the list of signed in players and use home.ftl
        if (httpSession.attribute(PostSigninRoute.PLAYER_KEY) != null){
            response.redirect(WebServer.HOME_URL);
            // render the View
            return null;

        }else {
            //initialize the VM
            Map<String, Object> vm = new HashMap<>();
            vm.put(TITLE_KEY, TITLE);
            vm.put(MSG_KEY, MSG_VAL);

            // render the View
            return templateEngine.render(new ModelAndView(vm , VIEW_NAME));
        }
    }
}