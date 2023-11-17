package com.webcheckers.ui;

import static spark.Spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import spark.TemplateEngine;


/**
 * The server that initializes the set of HTTP request handlers.
 * This defines the <em>web application interface</em> for this
 * WebCheckers application.
 *
 * <p>
 * There are multiple ways in which you can have the client issue a
 * request and the application generate responses to requests. If your team is
 * not careful when designing your approach, you can quickly create a mess
 * where no one can remember how a particular request is issued or the response
 * gets generated. Aim for consistency in your approach for similar
 * activities or requests.
 * </p>
 *
 * <p>Design choices for how the client makes a request include:
 * <ul>
 *     <li>Request URL</li>
 *     <li>HTTP verb for request (GET, POST, PUT, DELETE and so on)</li>
 *     <li><em>Optional:</em> Inclusion of request parameters</li>
 * </ul>
 * </p>
 *
 * <p>Design choices for generating a response to a request include:
 * <ul>
 *     <li>View templates with conditional elements</li>
 *     <li>Use different view templates based on results of executing the client request</li>
 *     <li>Redirecting to a different application URL</li>
 * </ul>
 * </p>
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author <a href='mailto:pjw7904@rit.edu'>Peter Willis</a>
 */
public class WebServer {
  private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

  /**
   * The URL pattern to request the Home page.
   */
  public static final String HOME_URL = "/";

  /**
   * The URL pattern to request the Sign-in page.
   */
  public static final String SIGNIN_URL = "/signin";

  /**
   * The URL pattern to request the Game page.
   */
  public static final String GAME_URL = "/game";

  /**
   * The URL pattern to post a Sign-out request.
   */
  public static final String SIGNOUT_URL = "/signout";

  public static final String VALIDATE_MOVE_URL = "/validateMove";
  public static final String SUBMIT_TURN_URL = "/submitTurn";
  public static final String BACKUP_MOVE_URL = "backupMove";
  public static final String CHECK_TURN_URL = "/checkTurn";
  public static final String RESIGN_GAME_URL = "resignGame";

  /**
   * The URL pattern to request the Game page as a spectator.
   */
  public static final String SPECTATOR_GAME_URL = "/spectator/game";

  /**
   * The URL pattern to check if there was an update to the game being spectated.
   */
  public static final String SPECTATOR_CHECK_TURN_URL = "/spectator/checkTurn";

  /**
   * The URL pattern to stop spectating a game.
   */
  public static final String SPECTATOR_STOP_URL = "/spectator/stopWatching";

  //
  // Attributes
  //

  private final PlayerLobby lobby;
  private final GameCenter gameCenter;
  private final TemplateEngine templateEngine;
  private final Gson gson;


  /**
   * The constructor for the Web Server.
   * @param templateEngine
   *    The default {@link TemplateEngine} to render page-level HTML views.
   * @param gson
   *    The Google JSON parser object used to render Ajax responses.
   * @throws NullPointerException
   *    If any of the parameters are {@code null}.
   */
  public WebServer(final TemplateEngine templateEngine, final Gson gson, final PlayerLobby lobby, final GameCenter gameCenter) {
    // validation
    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    Objects.requireNonNull(gson, "gson must not be null");
    Objects.requireNonNull(lobby, "lobby must not be null");
    Objects.requireNonNull(gameCenter, "Game center must not be null");
    //
    this.templateEngine = templateEngine;
    this.lobby = lobby;
    this.gson = gson;
    this.gameCenter = gameCenter;
  }

  /**
   * Initialize all of the HTTP routes that make up this web application.
   * <p>
   * Initialization of the web server includes defining the location for static
   * files, and defining all routes for processing client requests. The method
   * returns after the web server finishes its initialization.
   * </p>
   */
  public void initialize() {

    // Configuration to serve static files
    staticFileLocation("/public");

    // Shows the Checkers game Home page.
    get(HOME_URL, new GetHomeRoute(lobby,gameCenter, templateEngine));

    // Shows the Checkers game Sign-in page.
    get(SIGNIN_URL, new GetSigninRoute(templateEngine));

    // Show Checkers game page.
    get(GAME_URL, new GetGameRoute(templateEngine, gson, lobby, gameCenter));

    // Show Checkers game page to a spectator.
    get(SPECTATOR_GAME_URL, new GetSpectatorGameRoute(templateEngine, gson, lobby, gameCenter));

    // Show Checkers game page to a spectator.
    get(SPECTATOR_STOP_URL, new GetSpectatorStopWatchingRoute(templateEngine, gson, lobby, gameCenter));

    // Post a potential name to sign-in as.
    post(SIGNIN_URL, new PostSigninRoute(lobby, templateEngine));

    // Post the desire to sign-out.
    post(SIGNOUT_URL, new PostSignoutRoute(lobby, templateEngine));

    // Post validate move request
    post(VALIDATE_MOVE_URL, new PostValidateMoveRoute(templateEngine, gson, gameCenter));

    // Post submit turn request
    post(SUBMIT_TURN_URL, new PostSubmitTurnRoute(templateEngine, gson, lobby, gameCenter));

    // Post backup move request
    post(BACKUP_MOVE_URL, new PostBackupMoveRoute(gson, gameCenter));

    // Post check turn request
    post(CHECK_TURN_URL, new PostCheckTurnRoute(gson, gameCenter));

    // Post resign game request
    post(RESIGN_GAME_URL, new PostResignGameRoute(templateEngine, gson, lobby, gameCenter));

    // Post resign game request
    post(SPECTATOR_CHECK_TURN_URL, new PostSpectatorCheckTurnRoute(templateEngine, gson, gameCenter));

    // Log message confirming the Webserver is ready to go
    LOG.config("WebServer is initialized.");
  }
}