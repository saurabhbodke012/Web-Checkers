package com.webcheckers.model;

import com.webcheckers.util.ValidationHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Stores the board and other data for the game
 */
public class Game {
    private static final Logger LOG = Logger.getLogger(Game.class.getName());

    private String redPlayerName;
    private String whitePlayerName;
    private Piece.Color activeColor;
    private Board board;
    private ArrayList<Move> moveSequence;
    private int movesMade;
    private String gameEndMsg; // declaring global private string gameEndMsg
    private String winner; // declaring global private string winner
    private ValidationHelper helper;
    private boolean gameOver; // declaring global attribute of gameOver

    private String gameID; // A unique ID associated with a given game


    /**
     * Constructor for the class Game
     * @param board
     * @param redPlayerName
     * @param whitePlayerName
     * @param activeColor
     */
    public Game(Board board,String redPlayerName, String whitePlayerName, Piece.Color activeColor,ValidationHelper helper) {
        this.redPlayerName = redPlayerName;
        this.whitePlayerName = whitePlayerName;
        this.activeColor = activeColor;
        this.board = board;
        this.moveSequence = null;
        this.movesMade = 0;
        this.gameOver = false; // setting the value of gameOver to be false
        this.gameEndMsg = null; // setting the value of gameEndMsg to NULL
        this.winner = null; // setting the value of winner to NULL
        this.helper = helper;
        this.gameID = generateGameID();

        LOG.config(String.format("Game %s has been created", gameID));
    }

    /**
     *Getters methods
     */

    public String getRedPlayerName() {
        return redPlayerName;
    }

    public String getWhitePlayerName() {
        return whitePlayerName;
    }

    public Piece.Color getActiveColor() {
        return activeColor;
    }

    public Board getBoard() {
        return board;
    }

    public String getGameID() {
        return gameID;
    }

    /**
     * getter method for second pending move to be made
     */
    public ArrayList<Move> getMoveSequence() {
        return moveSequence;
    }

    /**
     * Generates an identifer for a game
     * @return return the generated ID for the game
     */
    public String generateGameID() {
        // The first half of the gameID will be the last part of a random UUID
        String gameUUID = UUID.randomUUID().toString();
        gameUUID = gameUUID.substring(gameUUID.lastIndexOf('-') + 1);

        // The second half of the gameID will be a random number between 0-10000
        int randValue = new Random().nextInt(10000);

        String gameID = gameUUID + randValue;

        return gameID;
    }

    public void setMoveSequence(ArrayList<Move> moveSequence) {
        this.moveSequence = moveSequence;
    }

    public Move getNextMoveToMake(){
        if (movesMade == moveSequence.size()){
            return null;
        }
        return moveSequence.get(movesMade);
    }

    public void MakeMoves(){
        if (moveSequence != null){
            for (Move move : moveSequence){
                System.out.println("Making the move: " + move);
                board.makeMove(move, activeColor);
            }
        }

        moveSequence = null;
        movesMade = 0;

        checkEndGame();
        changeActiveColor();
    }


    public boolean amIActive(Player player){

        String name = player.getName();
        if(name.equals(redPlayerName) && activeColor == Piece.Color.RED){ // check if player is red and active color is also red
            return true;
        }else if(name.equals(whitePlayerName) && activeColor == Piece.Color.WHITE){ // check if the player is white and active color is white
            return true;
        }
        return false;
    }

    /**
     * clearMoves method to clear the move
     */
    public void clearMoves(){
        if (movesMade > 0){
            if (movesMade == 1){
                moveSequence = null;
            }
            movesMade -= 1;
        }
    }

    /**
     * emptyState method to check is in empty state
     * @return return null
     */
    public boolean emptyState() {
        return moveSequence==null;
    }

    public void resign(String name) {
        gameOver = true;
        gameEndMsg = name + " resigned.";
        winner = getOpponentTo(name);
    }

    /**
     get oppenent name
     */
    public String getOpponentTo(String name) {
        if (name.equals(redPlayerName)) return whitePlayerName;
        else return redPlayerName;
    }


    public void changeActiveColor(){
        if (activeColor == Piece.Color.RED){
            activeColor = Piece.Color.WHITE;
        }else{
            activeColor = Piece.Color.RED;
        }
    }

    public boolean gameOver() {
        return gameOver;
    }

    /**
     * getter method for the winner of the game
     */
    public String getWinner() {
        return winner;
    }

    /**
     * getter method for the endgame message
     */
    public String getGameEndMsg() {
        return gameEndMsg;
    }

    public String generatePiecesMsg() {
        int pieces = helper.piecesOfColor(board,activeColor);
        return "You have " + Integer.toString(pieces) + " pieces remaining.";
    }

    /**
     * get the # moves (method for testing)
     * @return the number of moves made
     */
    public int getMovesMade() {
        return movesMade;
    }

    public List<String> generateMovesList() {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        ArrayList<ArrayList<Move>> possibleJumps = new ArrayList<>();
        helper.scanBoard(board,activeColor,possibleMoves,possibleJumps);
        List<String> movesList = new ArrayList<String>();
        if (possibleJumps.size() > 0) {
            for (ArrayList<Move> moveSequence : possibleJumps){
                String sequenceString = "";
                for (int i=0; i<moveSequence.size(); i++) {
                    sequenceString += "Jump " + (i+1) + " :  " + moveSequence.get(i).altString(activeColor);
                    if (i < moveSequence.size()-1) {
                        sequenceString += " | ";
                    }
                }
                movesList.add(sequenceString);}

        } else {
            if (activeColor == Piece.Color.WHITE) {
                for (Move move : possibleMoves) {
                    movesList.add(move.altString(activeColor));}
            } else {
                for (int i = possibleMoves.size()-1; i >= 0; i--) {
                    Move thisMove = possibleMoves.get(i);
                    movesList.add(thisMove.altString(activeColor));
                }
            }
        }

        return movesList;
    }

    public void checkEndGame()  {

        int redPieces = helper.piecesOfColor(board,Piece.Color.RED);
        if (redPieces == 0) {
            gameOver = true;
            gameEndMsg = redPlayerName + " ran out of pieces!"; // concatinating and updating the string
            winner = whitePlayerName;
            return;
        }
        int whitePieces = helper.piecesOfColor(board,Piece.Color.WHITE);
        if (whitePieces == 0) {
            gameOver = true;
            gameEndMsg = whitePlayerName + " ran out of pieces!";
            winner = redPlayerName;
            return;
        }

        Piece.Color inactiveColor;
        if (activeColor == Piece.Color.RED) inactiveColor = Piece.Color.WHITE;
        else inactiveColor = Piece.Color.RED;
        ArrayList<Move> possibleMoves = new ArrayList<>();
        ArrayList<ArrayList<Move>> possibleJumps = new ArrayList<>();
        helper.scanBoard(board,inactiveColor,possibleMoves, possibleJumps);
        if (possibleMoves.size()==0 && possibleJumps.size() == 0) {
            gameOver=true;
            if (inactiveColor == Piece.Color.WHITE) {
                gameEndMsg = whitePlayerName + " has been blocked!";
                winner = redPlayerName;
            } else {
                gameEndMsg = redPlayerName + " has been blocked!"; // concatenating and updating the string
                winner = whitePlayerName;
            }
            return;
        }
    }

    public boolean allMovesMade() {
        if (moveSequence != null){
            return moveSequence.size() == movesMade;
        }else{
            return false;
        }
    }

    /**
     * method for testing, set the game ID
     * @param id the id
     */
    public void setGameID(String id) {
        this.gameID = id;
    }

    public Move getLastMoveToMake(){
        return moveSequence.get(moveSequence.size()-1);
    }


    public void incrementMovesMade(){
        movesMade += 1;
    }

    /*
     * capture top left (if red player is active)
     * @param start the starting position
     * @return the new Move if it's a valid one, null if not
     */
    /*
    public Move redCaptureTopLeft(Position start) {
        Position startCopy = start;
        Piece.Color red = Piece.Color.RED;
        Move result = null;

        int redNewRow = startCopy.getRow() - 2;
        int redNewCol = startCopy.getCell() - 2;
        Position redEnd = new Position(redNewRow, redNewCol);


        //if it's the red player's turn and a jump is available
        if (activeColor == red) {
            //if the move is invalid return null
            if (!jumpAvailableTowardsWhiteSide(new Move(startCopy, redEnd), red)) {
                    return null;
            }

            while (jumpAvailableTowardsWhiteSide(new Move(startCopy, redEnd), red)) {
                //jump over the white piece and capture it (delete it from the board)

                //remove white piece from top left first
                board.getRow(startCopy.getRow() - 1).getSpace(startCopy.getCell() - 1).removePiece();

                //jump up-left-diagonally 2 spaces
                board.getRow(redEnd.getRow()).getSpace(redEnd.getCell()).updatePiece(new Piece(Piece.Type.SINGLE, red));

                //remove the red piece from its old location
                board.getRow(startCopy.getRow()).getSpace(startCopy.getCell()).removePiece();

                //record the move that was made
                startCopy = redEnd;
                result = new Move(startCopy, redEnd);
            }
            return result;
        }
        //it's the white player's turn
        return null;
    }
    */


    /*
     * capture top right (if red player is active)
     * @param start the starting position
     * @return the new Move if it's a valid one, null if not

    public Move redCaptureTopRight(Position start) {
        Position startCopy = start;
        Piece.Color red = Piece.Color.RED;
        Move result = null;

        int redNewRow = startCopy.getRow() - 2;
        int redNewCol = startCopy.getCell() + 2;
        Position redEnd = new Position(redNewRow, redNewCol);


        //if it's the red player's turn and a jump is available
        if (activeColor == red) {
            //if the move is invalid return null
            if (!jumpAvailableTowardsWhiteSide(new Move(startCopy, redEnd), red)) {
                return null;
            }

            while (jumpAvailableTowardsWhiteSide(new Move(startCopy, redEnd), red)) {
                //jump over the white piece and capture it (delete it from the board)

                //remove white piece from top right first
                board.getRow(startCopy.getRow() - 1).getSpace(startCopy.getCell() + 1).removePiece();

                //jump up-right-diagonally 2 spaces
                board.getRow(redEnd.getRow()).getSpace(redEnd.getCell()).updatePiece(new Piece(Piece.Type.SINGLE, red));

                //remove the red piece from its old location
                board.getRow(startCopy.getRow()).getSpace(startCopy.getCell()).removePiece();

                //record the move that was made
                startCopy = redEnd;
                result = new Move(startCopy, redEnd);
            }
            return result;
        }
        //it's the white player's turn
        return null;
    }

    * */

    /*
     * capture piece to the bottom right (if white player is active)
     * @param start the starting position
     * @return the new Move if it's a valid one, null if not
     *
    public Move whiteCaptureBottomRight(Position start) {
        Position startCopy = start;
        Piece.Color white = Piece.Color.WHITE;
        Move result = null;

        int whiteNewRow = startCopy.getRow() + 2;
        int whiteNewCol = startCopy.getCell() + 2;
        Position whiteEnd = new Position(whiteNewRow, whiteNewCol);


        //if it's the white player's turn and a jump is available
        if (activeColor == white) {
            //if the move is invalid return null
            if (!jumpAvailableTowardsWhiteSide(new Move(startCopy, whiteEnd), white)) {
                return null;
            }

            while (jumpAvailableTowardsWhiteSide(new Move(startCopy, whiteEnd), white)) {
                //jump over the red piece and capture it (delete it from the board)

                //remove red piece from bottom right first
                board.getRow(startCopy.getRow() + 1).getSpace(startCopy.getCell() + 1).removePiece();

                //jump down-right-diagonally 2 spaces
                board.getRow(whiteEnd.getRow()).getSpace(whiteEnd.getCell()).updatePiece(new Piece(Piece.Type.SINGLE, white));

                //remove the white piece from its old location
                board.getRow(startCopy.getRow()).getSpace(startCopy.getCell()).removePiece();

                //record the move that was made
                startCopy = whiteEnd;
                result = new Move(startCopy, whiteEnd);
            }
            return result;
        }
        //it's the red player's turn
        return null;
    }

     */

    /*
     * capture piece to the bottom left (if white player is active)
     * @param start the starting position
     * @return the new Move if it's a valid one, null if not
     *
    public Move whiteCaptureBottomLeft(Position start) {
        Position startCopy = start;
        Piece.Color white = Piece.Color.WHITE;
        Move result = null;

        int whiteNewRow = startCopy.getRow() + 2;
        int whiteNewCol = startCopy.getCell() - 2;
        Position whiteEnd = new Position(whiteNewRow, whiteNewCol);


        //if it's the white player's turn and a jump is available
        if (activeColor == white) {
            //if the move is invalid return null
            if (!jumpAvailableTowardsWhiteSide(new Move(startCopy, whiteEnd), white)) {
                return null;
            }

            while (jumpAvailableTowardsWhiteSide(new Move(startCopy, whiteEnd), white)) {
                //jump over the red piece and capture it (delete it from the board)

                //remove red piece from bottom right first
                board.getRow(startCopy.getRow() + 1).getSpace(startCopy.getCell() - 1).removePiece();

                //jump down-left-diagonally 2 spaces
                board.getRow(whiteEnd.getRow()).getSpace(whiteEnd.getCell()).updatePiece(new Piece(Piece.Type.SINGLE, white));

                //remove the white piece from its old location
                board.getRow(startCopy.getRow()).getSpace(startCopy.getCell()).removePiece();

                //record the move that was made
                startCopy = whiteEnd;
                result = new Move(startCopy, whiteEnd);
            }
            return result;
        }
        //it's the red player's turn
        return null;
    }

     */

    /*
    public boolean jumpAvailableTowardsWhiteSide(Move move, Piece.Color playerColor) {
        boolean result;

        int startRowIndex = move.getStart().getRow();
        int startSpaceIndex = move.getStart().getCell();

        if(startRowIndex == 1) {
            result = false;
        }

        else {
            int nextRowIndex = startRowIndex - 1;
            int potentialJumpRowIndex = startRowIndex - 2;

            result = jumpIsAvailable(nextRowIndex, startSpaceIndex, potentialJumpRowIndex, playerColor);
        }

        return result;
    }


    public boolean jumpAvailableTowardsRedSide(Move move, Piece.Color playerColor) {
       boolean result;

        int startRowIndex = move.getStart().getRow();
        int startSpaceIndex = move.getStart().getCell();

        if(startRowIndex == 6) {
            result = false;
        }

        else {
            int nextRowIndex = startRowIndex + 1;
            int potentialJumpRowIndex = startRowIndex + 2;

            result = jumpIsAvailable(nextRowIndex, startSpaceIndex, potentialJumpRowIndex, playerColor);
        }

        return result;
    }

    public boolean jumpIsAvailable(int nextRowIndex, int startSpaceIndex, int potentialJumpRowIndex, Piece.Color playerColor) {
        // Check space on the row above the starting row, on the space to the LEFT
        int leftSpaceIndex = startSpaceIndex-1;
        int leftJumpSpaceIndex = startSpaceIndex-2;
        if(leftSpaceIndex > 0) {
            Piece.Color spaceColor = getSpacePieceColor(nextRowIndex, leftSpaceIndex);
            //if an enemy piece is there
            if(spaceColor != null && spaceColor != playerColor) {
                spaceColor = getSpacePieceColor(potentialJumpRowIndex, leftJumpSpaceIndex);
                if(spaceColor == null) {
                    return true;
                }
            }
        }

        // Check space on the row above the starting row, on the space to the RIGHT
        int rightSpaceIndex = startSpaceIndex + 1;
        int rightJumpSpaceIndex = startSpaceIndex + 2;
        if(rightSpaceIndex < 7) {
            Piece.Color spaceColor = getSpacePieceColor(nextRowIndex, rightSpaceIndex);
            //if an enemy piece is there
            if(spaceColor != null && spaceColor != playerColor) {
                spaceColor = getSpacePieceColor(potentialJumpRowIndex, rightJumpSpaceIndex);
                if(spaceColor == null) {
                    return true;
                }
            }
        }

        return false;
    }

     */

    /*
    public Piece.Color getSpacePieceColor(int rowIndex, int spaceIndex) {
        Space space = board.getRow(rowIndex).getSpace(spaceIndex);

        if(space.getPiece() != null) {
            return space.getPiece().getColor();
        }

        else {
            return null;
        }
    }

     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(redPlayerName, game.redPlayerName) && Objects.equals(whitePlayerName, game.whitePlayerName) && activeColor == game.activeColor && Objects.equals(board, game.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(redPlayerName, whitePlayerName, activeColor, board);
    }
}
//End of Class
