package com.webcheckers.ui;

import static spark.Spark.halt;

import java.util.*;
import java.util.logging.Logger;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

import com.webcheckers.util.Message;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author <a href='mailto:pjw7904@rit.edu'>Peter Willis</a>
 */
public class GetHomeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers (you are not signed in).");
  static final Message INSTRUCTIONS_MSG = Message.info("Please select an opponent");

  static final String MSG_ATTR = "message";
  public static final String TITLE_ATTR = "title";
  static final String USER_ATTR = "currentUser";
  static final String PLAYER_COUNT_ATTR = "numberOfLoggedInPlayers";
  public static final String PLAYERS_LIST_ATTR = "players";
  static final String NUMBER_PLAYERS_MSG = "There are %d player(s) currently signed in.";
  static final String AVAILABLE_PLAYER_KEY = "availablePlayers";
  static final String TITLE_LOGGED_OUT = "Welcome!";
  static final String TITLE_LOGGED_IN = "Lets play!";
  static final String VIEW_NAME = "home.ftl";

  public static final String UPDATED_STATUS_MSG = "updatedMsg";

  private final TemplateEngine templateEngine;
  private final PlayerLobby lobby;
  private final GameCenter gameCenter;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetHomeRoute(final PlayerLobby lobby,GameCenter gameCenter, TemplateEngine templateEngine) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.gameCenter = Objects.requireNonNull(gameCenter, "templateEngine is required");
    this.lobby = Objects.requireNonNull(lobby, "lobby must not be null");
    LOG.config("GetHomeRoute is initialized.");
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
    LOG.info("GetHomeRoute is invoked.");

    // retrieve the HTTP session
    final Session httpSession = request.session();
    Player currentPlayer = httpSession.attribute(PostSigninRoute.PLAYER_KEY);

    //create the hashmap
    Map<String, Object> vm = new HashMap<>();
    ModelAndView mv;

    // if the player is signed in, show the list of signed in players
    if(currentPlayer != null) {
      // check if player is in a game already
      if (gameCenter.getGame(currentPlayer.getName()) != null) {
        response.redirect(WebServer.GAME_URL);
        halt();
        return null;
      }

      Message updateMsg = httpSession.attribute(UPDATED_STATUS_MSG);

      mv = signedInView(vm, currentPlayer, updateMsg);
    }

    else {
      mv = signedOutView(vm);
    }

    // render the view
    return templateEngine.render(mv);
  }

  /**
   * get the signed in view
   * @param vm the vm
   * @param player the player
   * @return the view
   */
  private ModelAndView signedInView(final Map<String, Object> vm, Player player, Message updateMsg) {
    vm.put(TITLE_ATTR, TITLE_LOGGED_IN);

    if(updateMsg != null) {
      vm.put(MSG_ATTR, updateMsg);
    }

    else {
      vm.put(MSG_ATTR, INSTRUCTIONS_MSG);
    }

    // The current user, who is logged in.
    vm.put(USER_ATTR, player);

    //add each online player
    ArrayList<Player> players = lobby.getPlayers();
    ArrayList<Player> copy = new ArrayList<>(players);
    copy.remove(player);
    vm.put(PLAYERS_LIST_ATTR, copy);

    return new ModelAndView(vm, VIEW_NAME);
  }

  /**
   * get the signed out view
   * @param vm the vm
   * @return the view
   */
  private ModelAndView signedOutView(final Map<String, Object> vm) {
    vm.put(TITLE_ATTR, TITLE_LOGGED_OUT);
    vm.put(MSG_ATTR, WELCOME_MSG);

    // Show number of signed-in users to player
    List<Player> players = lobby.getPlayers();
    int playersSize = players.size();
    boolean availablePlayers = playersSize>0;

    vm.put(AVAILABLE_PLAYER_KEY, availablePlayers);
    vm.put(PLAYER_COUNT_ATTR, String.format(NUMBER_PLAYERS_MSG, playersSize));

    return new ModelAndView(vm, VIEW_NAME);
  }
} // End of class