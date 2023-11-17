package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

/**
 * The UI Controller to GET the Game page from the perspective of a spectator
 *
 * @author <a href='mailto:pjw7904@rit.edu'>Peter Willis</a>
 */
public class GetSpectatorGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetSpectatorGameRoute.class.getName());

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
    public GetSpectatorGameRoute(TemplateEngine templateEngine, Gson gson, PlayerLobby lobby, GameCenter gameCenter) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.lobby = Objects.requireNonNull(lobby, "lobby must not be null");
        this.gameCenter = Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        this.gson = Objects.requireNonNull(gson, "gson must not be null");

        LOG.config("GetSpectatorGameRoute is initialized.");
    }

    /**
     * Render the WebCheckers Game Page
     * @param request - the HTTP request
     * @param response - he HTTP response
     * @return - it returns the rendered HTML for the game page
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("GetSpectatorGameRoute is invoked.");

        final Session httpSession = request.session();

        // Grab the session player information, if valid.
        spectator = httpSession.attribute(PostSigninRoute.PLAYER_KEY);

        // If the spectator isn't logged in, push them to the home page
        if (spectator == null) {
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }

        // If the spectator is in a game, push them to the game page
        if(gameCenter.getGame(spectator.getName()) != null) {
            response.redirect(WebServer.GAME_URL);
            halt();
            return null;
        }

        // Create the hashmap
        Map<String, Object> vm = new HashMap<>();
        ModelAndView mv;

        // Get the game ID
        String gameID = gson.fromJson(request.queryParams("gameID"), String.class);
        Game spectatingGame = gameCenter.getGameFromID(gameID);

        // If the game cannot be found via its ID, try a query parameter for the chosen player
        if (spectatingGame == null) {
            String spectatingPlayer = request.queryParams("name");
            spectatingGame = gameCenter.getGame(spectatingPlayer);

            // If the game still cannot be found, push them to the home page
            if(spectatingGame == null) {
                response.redirect(WebServer.HOME_URL);
                halt();
                return null;
            }
        }

        // Handle end-of-game use case, push the user back to the home screen.
        if(spectatingGame.gameOver()) {
            httpSession.attribute(GetHomeRoute.UPDATED_STATUS_MSG,  Message.info(spectatingGame.getGameEndMsg()));
            httpSession.removeAttribute(PostSpectatorCheckTurnRoute.TURN_KEY);

            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }

        // place the active player's turn color into a session attribute so that it may be checked during check-turn
        httpSession.attribute(PostSpectatorCheckTurnRoute.TURN_KEY, spectatingGame.getActiveColor());

        boolean whitePlayersTurn = spectatingGame.getActiveColor() == Piece.Color.WHITE;
        mv = updateSpectateModeState(vm, spectatingGame, "Spectating", whitePlayersTurn);

        // render the view
        return templateEngine.render(mv);
    }

    private ModelAndView updateSpectateModeState(final Map<String, Object> vm, Game game, String titleValue, boolean isFlippedForWhitePlayer) {
        vm.put(GetGameRoute.TITLE_KEY, titleValue);

        /* GAME DATA */
        vm.put(GetGameRoute.ID_KEY, gson.toJson(game.getGameID()));                              // gameID
        vm.put(GetGameRoute.CURR_USER_KEY, spectator);                                           // currentUser
        vm.put(GetGameRoute.VIEW_MODE_KEY, GetGameRoute.gameMode.SPECTATOR);                     // viewMode
        vm.put(GetGameRoute.RED_PLAYER_KEY, lobby.getPlayer(game.getRedPlayerName()));           // redPlayer
        vm.put(GetGameRoute.WHITE_PLAYER_KEY, lobby.getPlayer(game.getWhitePlayerName()));       // whitePlayer
        vm.put(GetGameRoute.ACTIVE_COLOR_KEY, game.getActiveColor());                            // activeColor
        vm.put(GetGameRoute.BOARD_KEY, game.getBoard());                                         // board

        // Flip the board if the white player is the active player
        vm.put(GetGameRoute.FLIPPED_KEY, isFlippedForWhitePlayer);

        return new ModelAndView(vm, GetGameRoute.GAME_FTL);
    }
}
