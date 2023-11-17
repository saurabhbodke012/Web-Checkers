package com.webcheckers.ui;
import java.util.*;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.*;
import com.webcheckers.util.ValidationHelper;
import spark.*;

import com.webcheckers.util.Message;

public class PostValidateMoveRoute implements Route{

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final Gson gson;
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /signout} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostValidateMoveRoute(final TemplateEngine templateEngine, Gson gson, GameCenter gameCenter) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(gson, "gson must not be null");
        Objects.requireNonNull(gameCenter, "Game center must not be null");
        this.templateEngine = templateEngine;
        this.gson = gson;
        this.gameCenter = gameCenter;

        LOG.config("PostValidateMoveRoute is initialized.");
    }


    /**
     * Render the WebCheckers Game page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for validate move page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.info("PostValidateMoveRoute is invoked.");
        // retrieve the HTTP session
        final Session httpSession = request.session();

        ArrayList<Move> possibleMoves = new ArrayList<>();
        ArrayList<ArrayList<Move>> possibleJumps = new ArrayList<>();


        Move move_made = gson.fromJson(request.queryParams("actionData"), Move.class);
        System.out.println(move_made);

        Player thisPlayer = httpSession.attribute(PostSigninRoute.PLAYER_KEY);
        Game game = null;
        if (thisPlayer != null){
            game = gameCenter.getGame(thisPlayer.getName());
        }

        if (move_made != null && game != null) {
            Board board = game.getBoard();
            Piece.Color activeColor = game.getActiveColor();

            if (game.getMoveSequence() != null){
                if (move_made.equals(game.getLastMoveToMake())){
                    game.incrementMovesMade();
                    return gson.toJson(Message.info("The move is the last continuation of a jump."));
                }
                else if (move_made.equals(game.getNextMoveToMake())){
                    game.incrementMovesMade();
                    return gson.toJson(Message.info("The move is valid continuation of a jump."));
                }
                return gson.toJson(Message.error("You must continue the jump."));
            }

            new ValidationHelper().scanBoard(board,activeColor,possibleMoves, possibleJumps);


            if(possibleJumps.size() > 0){
                for(ArrayList<Move> sequence : possibleJumps){
                    if(move_made.equals(sequence.get(0))){
                        if (sequence.size() > 1){
                            game.setMoveSequence(sequence);
                            game.incrementMovesMade();
                            return gson.toJson(Message.info("The move is a start to a multiple jump."));
                        }else {
                            game.setMoveSequence(sequence);
                            game.incrementMovesMade();
                            return gson.toJson(Message.info("The move is a valid single jump."));
                        }
                    }
                }
                return gson.toJson(Message.error("You must take a valid jump."));
            }else if(possibleMoves.size() > 0){
                for(Move temp_move: possibleMoves){
                    if(move_made.equals(temp_move)){
                        ArrayList<Move> singleSequence = new ArrayList<>();
                        singleSequence.add(move_made);
                        game.setMoveSequence(singleSequence);
                        game.incrementMovesMade();
                        return gson.toJson(Message.info("The move is a valid move."));
                    }
                }
                return  gson.toJson(Message.error("The move is not a valid move."));
            }
        }
        response.redirect(WebServer.HOME_URL);
        return null;

    }
}

