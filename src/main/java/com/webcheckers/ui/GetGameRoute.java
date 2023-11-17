package com.webcheckers.ui;

import com.google.gson.Gson;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;

import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;

import com.webcheckers.util.Message;
import com.webcheckers.util.ValidationHelper;

import spark.*;
import static spark.Spark.halt;

import java.util.*;
import java.util.logging.Logger;

/**
 * A route to rend the Game page
 * which is invoked by Get/Game
 */
public class GetGameRoute implements Route {
    // Attribute keys for the vm
    private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());

    public static final String TITLE_KEY = "title";
    public static final String VIEW_MODE_KEY = "viewMode";
    public static final String ID_KEY = "gameID";
    public static final String CURR_USER_KEY = "currentUser";
    public static final String MESSAGE_KEY = "message";
    public static final String RED_PLAYER_KEY = "redPlayer";
    public static final String WHITE_PLAYER_KEY = "whitePlayer";
    public static final String ACTIVE_COLOR_KEY = "activeColor";
    public static final String BOARD_KEY = "board";
    public static final String FLIPPED_KEY = "flipped";
    public static final String MODE_OPTIONS_KEY = "modeOptionsAsJSON";

    public static final Message GAME_MESSAGE_VAL = Message.info("Game On!");
    public static final Message ERROR_MESSAGE_VAL = Message.error("The user you selected is already in a game. Try another!");

    public static final String GAME_FTL = "game.ftl";


    public enum gameMode {
        PLAY,
        SPECTATOR
    }

    private final TemplateEngine templateEngine;
    private final PlayerLobby lobby;
    private final GameCenter gameCenter;
    private final Gson gson;

    // the player associated with the session
    private Player firstPlayer;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     * @param templateEngine the HTML template rendering engine
     */
    public GetGameRoute(TemplateEngine templateEngine,Gson gson, PlayerLobby lobby, GameCenter gameCenter) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.lobby = Objects.requireNonNull(lobby, "lobby must not be null");
        this.gameCenter = Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        this.gson = Objects.requireNonNull(gson, "gson must not be null");

        LOG.config("GetGameRoute is initialized.");
    }

    /**
     * Render the WebCheckers Game Page
     * @param request - the HTTP request
     * @param response - he HTTP response
     * @return - it returns the rendered HTML for the game page
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("GetGameRoute is invoked.");

        final Session httpSession = request.session();

        // Grab the session player information, if valid.
        firstPlayer = httpSession.attribute(PostSigninRoute.PLAYER_KEY);

        if (firstPlayer == null) {
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }

        // Create the hashmap
        Map<String, Object> vm = new HashMap<>();
        ModelAndView mv;

        // Current game information, from the perspective of the session player
        Game currentGame = gameCenter.getGame(firstPlayer.getName());

        // If the session player is not currently in a game
        if (currentGame == null) {
            String opponentPicked = request.queryParams("name");
            mv = attemptToStartNewGame(vm, opponentPicked);
        }

        else {
            // Orient board accordingly depending on if the player is the red or white player
            boolean isWhitePlayer = lobby.getPlayer(currentGame.getWhitePlayerName()).equals(firstPlayer);

            updatePlayModeState(vm, currentGame, "Game Ongoing", isWhitePlayer);

            // If the session player's game is now over
            if(currentGame.gameOver()) {
                mv = endGame(vm, currentGame);
            }

            else {
                mv = new ModelAndView(vm, GAME_FTL);
            }
        }

        // render the view
        return templateEngine.render(mv);
    }

    private void updatePlayModeState(final Map<String, Object> vm, Game game, String titleValue, boolean isFlippedForWhitePlayer) {
        vm.put(TITLE_KEY, titleValue);

        /* GAME DATA */
        vm.put(ID_KEY, gson.toJson(game.getGameID()));                              // gameID
        vm.put(CURR_USER_KEY, firstPlayer);                                         // currentUser
        vm.put(VIEW_MODE_KEY, gameMode.PLAY);                                       // viewMode
        vm.put(RED_PLAYER_KEY, lobby.getPlayer(game.getRedPlayerName()));           // redPlayer
        vm.put(WHITE_PLAYER_KEY, lobby.getPlayer(game.getWhitePlayerName()));       // whitePlayer
        vm.put(ACTIVE_COLOR_KEY, game.getActiveColor());                            // activeColor
        vm.put(BOARD_KEY, game.getBoard());                                         // board

        // Flip the board if the white player is the session player
        vm.put(FLIPPED_KEY, isFlippedForWhitePlayer);

        // Present the user with a message
        vm.put(MESSAGE_KEY, GAME_MESSAGE_VAL);

        //Add additional information if the user is active.
        if (game.amIActive(firstPlayer)) {
            vm.put("piecesMSG", game.generatePiecesMsg());
            vm.put("moves", game.generateMovesList());
        }
    }

    private ModelAndView attemptToStartNewGame(final Map<String, Object> vm, String opponentPicked) {
        ModelAndView mv;
        Player otherPlayer;

        otherPlayer = lobby.getPlayer(opponentPicked);

        if(gameCenter.getGame(otherPlayer.getName()) != null) {
            mv = userAlreadyInGame(vm);
        }

        else {
            // Create a new checkers game and note the players in the game
            Game game = new Game(new Board(), firstPlayer.getName(), otherPlayer.getName(), Piece.Color.RED, new ValidationHelper());

            gameCenter.addGame(firstPlayer.getName(), game);
            gameCenter.addGame(otherPlayer.getName(), game);

            firstPlayer.setInGameStatus(true);
            otherPlayer.setInGameStatus(true);

            updatePlayModeState(vm, game, "New Game", false);
            mv = new ModelAndView(vm, GAME_FTL);
        }

        // render the view
        return mv;
    }

    private ModelAndView userAlreadyInGame(final Map<String, Object> vm) {
        LOG.info("GetGameRoute-Error");

        List<Player> players = lobby.getPlayers();
        List<Player> duplicate = new ArrayList<>(players);
        duplicate.remove(firstPlayer);

        vm.put(TITLE_KEY,"Home");
        vm.put(GetHomeRoute.MSG_ATTR,ERROR_MESSAGE_VAL);
        vm.put(GetHomeRoute.PLAYERS_LIST_ATTR,duplicate);

        vm.put(GetHomeRoute.USER_ATTR, firstPlayer);
        // render the view
        return new ModelAndView(vm, GetHomeRoute.VIEW_NAME);
    }

    private ModelAndView endGame(final Map<String, Object> vm, Game game) {
        final Map<String, Object> modeOptions = new HashMap<>(2);

        modeOptions.put("isGameOver", true);

        String gameEndMsg = game.getGameEndMsg();
        String winner = game.getWinner();

        if (winner.equals(firstPlayer.getName())) {
            gameEndMsg += " You won the game!";
        }

        else {
            gameEndMsg += " " + winner + " won the game.";
        }

        modeOptions.put("gameOverMessage", gameEndMsg);
        vm.put(MODE_OPTIONS_KEY, gson.toJson(modeOptions));

        gameCenter.removeGameBoard(firstPlayer.getName());
        firstPlayer.setInGameStatus(false);

        return new ModelAndView(vm, GAME_FTL);
    }
}