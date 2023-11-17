package com.webcheckers.model;

import com.webcheckers.util.ValidationHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private Game game2;
    private Board board;
    private String redPlayerName;
    private String whitePlayerName;
    private Piece.Color color;
    private Piece.Color color2;
    private ValidationHelper helper;

    @BeforeEach
    public void setUp() {
        board = new Board();
        redPlayerName = "Naruto";
        whitePlayerName = "Sasuke";
        color = Piece.Color.RED;
        color2 = Piece.Color.WHITE;
        helper = mock(ValidationHelper.class);
        game = new Game(board, redPlayerName, whitePlayerName, color, helper);
        game2 = new Game(board, redPlayerName, whitePlayerName, color2, helper);
    }

    /**
     * Test that the accessors return the correct values
     */

    @Test
    public void accessorTest() {
        assertEquals(board, game.getBoard());
        assertEquals(redPlayerName, game.getRedPlayerName());
        assertEquals(whitePlayerName, game.getWhitePlayerName());
        assertEquals(color, game.getActiveColor());
    }


    /**
     * Test that a game is not null when instantiated
     */

    @Test
    public void nullTest() {
        assertNotNull(game);
    }


    /**
     * Test Equals
     */

    @Test
    public void equalsTest(){
        Game g1 = new Game(board, redPlayerName, whitePlayerName, color, helper);
        Game g2 = new Game(board, redPlayerName, whitePlayerName, color, helper);
        Game g3 = new Game(board, "hi", whitePlayerName, color, helper);
        Game g4 = new Game(board, "Stacy", whitePlayerName, color, helper);
        Game g5 = new Game(board, redPlayerName, "Stacy", color, helper);
        Game g6 = new Game(board, redPlayerName, whitePlayerName, color.WHITE, helper);

        Board b = new Board();
        Game g7 = new Game(b, redPlayerName, whitePlayerName, color, helper);

        assertEquals(g1, g1);
        assertEquals(g1, g2);
        assertNotEquals(g1, g3);
        assertNotEquals(b, g1);
        assertFalse(g1.equals(b));
        assertNotEquals(null, g1);
        assertFalse(g3.equals(null));
        assertNotEquals(g1, g4);
        assertNotEquals(g1, g5);
        assertNotEquals(g1, g6);
        assertNotEquals(g1, g7);
    }

    @Test
    public void getMoveSequence() {
        ArrayList<Move> moves = new ArrayList<>();
        moves.add(new Move(new Position(1, 2), new Position(3, 4)));
        game.setMoveSequence(moves);
        assertEquals("ArrayList", game.getMoveSequence().getClass().getSimpleName());
    }

    @Test
    public void getNextMoveToMake() {
        ArrayList<Move> moves = new ArrayList<>();
        game.setMoveSequence(moves);
        assertNull(game.getNextMoveToMake());
        moves.add(new Move(new Position(1, 2), new Position(3, 4)));
        moves.add(new Move(new Position(6, 7), new Position(4, 5)));
        game.setMoveSequence(moves);
        game.incrementMovesMade();
        assertEquals("Move", game.getNextMoveToMake().getClass().getSimpleName());
    }

    @Test
    public void MakeMoves() {
        game.MakeMoves();
        assertNull(game.getMoveSequence());
        assertEquals(0, game.getMovesMade());

        ArrayList<Move> moves = new ArrayList<>();
        moves.add(new Move(new Position(1, 2), new Position(3, 4)));
        moves.add(new Move(new Position(6, 7), new Position(4, 5)));
        game.setMoveSequence(moves);
        game.incrementMovesMade();

        game.MakeMoves();
        assertNull(game.getMoveSequence());
        assertEquals(0, game.getMovesMade());
    }

    @Test
    public void amIActive() {
        Player naruto = new Player("Naruto");
        Player sasuke = new Player("Sasuke");
        assertTrue(game.amIActive(naruto));
        assertFalse(game.amIActive(sasuke));
        game.MakeMoves();

        assertFalse(game.amIActive(naruto));
        assertTrue(game.amIActive(sasuke));
    }

    @Test
    public void clearMoves() {
        game.clearMoves();
        assertNull(game.getMoveSequence());

        game.incrementMovesMade();
        game.clearMoves();
        assertNull(game.getMoveSequence());

        game.incrementMovesMade();
        game.incrementMovesMade();
        game.clearMoves();
        assertNull(game.getMoveSequence());
    }

    @Test
    public void emptyState() {
        assertTrue(game.emptyState());

        ArrayList<Move> moves = new ArrayList<>();
        moves.add(new Move(new Position(1, 2), new Position(3, 4)));
        game.setMoveSequence(moves);

        assertFalse(game.emptyState());
    }

    @Test
    public void resign() {
        game.resign(whitePlayerName);

        assertTrue(game.gameOver());
        assertEquals("Sasuke resigned.", game.getGameEndMsg());
        assertEquals("Naruto", game.getWinner());
    }

    @Test
    public void getOpponentTo() {
        assertEquals(redPlayerName, game.getOpponentTo("randomName"));
        assertEquals(whitePlayerName, game.getOpponentTo(redPlayerName));
    }

    @Test
    public void generatePiecesMsg() {
        assertEquals("You have 0 pieces remaining.", game.generatePiecesMsg());
    }

    @Test
    public void allMovesMade() {
        assertFalse(game.allMovesMade());

        ArrayList<Move> moves = new ArrayList<>();
        moves.add(new Move(new Position(1, 2), new Position(3, 4)));
        game.setMoveSequence(moves);
        assertFalse(game.allMovesMade());

        game.incrementMovesMade();

        assertTrue(game.allMovesMade());
    }

    @Test
    public void getLastMoveToMake() {
        ArrayList<Move> moves = new ArrayList<>();
        moves.add(new Move(new Position(1, 2), new Position(3, 4)));
        game.setMoveSequence(moves);

        assertEquals("Move", game.getLastMoveToMake().getClass().getSimpleName());
    }

    @Test
    public void hashCodeTest() {
        Game gameDitto = new Game(board, redPlayerName, whitePlayerName, color, helper);
        assertEquals(game.hashCode(), gameDitto.hashCode());
    }

    @Test
    public void generateMovesList() {
        assertEquals("[]", game2.generateMovesList().toString());

        assertEquals("ArrayList", game.generateMovesList().getClass().getSimpleName());
        assertEquals("[]", game.generateMovesList().toString());

        game.MakeMoves();

        assertEquals("[]", game.generateMovesList().toString());
    }

    @Test
    public void checkEndGame() {
        Board noWhites = new Board();

        //remove whites
        for (Row row: noWhites) {
            for (Space space: row) {
                Piece piece = space.getPiece();
                Piece.Color pieceColor = null;
                if (piece != null){
                    pieceColor = piece.getColor();
                }
                if (pieceColor == Piece.Color.WHITE) {
                    space.removePiece();
                }
            }
        }

        Game noWhitesGame = new Game(noWhites, redPlayerName, whitePlayerName, color, helper);
        noWhitesGame.checkEndGame();
        assertTrue(noWhitesGame.gameOver());
        assertEquals("[]", noWhitesGame.generateMovesList().toString());

        Board noReds = new Board();

        //remove reds
        for (Row row: noReds) {
            for (Space space: row) {
                Piece piece = space.getPiece();
                Piece.Color pieceColor = null;
                if (piece != null){
                    pieceColor = piece.getColor();
                }
                if (pieceColor == Piece.Color.RED) {
                    space.removePiece();
                }
            }
        }

        Game noRedsGame = new Game(noReds, redPlayerName, whitePlayerName, Piece.Color.WHITE, helper);
        noRedsGame.checkEndGame();
        assertTrue(noRedsGame.gameOver());
        assertEquals("[]", noRedsGame.generateMovesList().toString());


        Board customBoard = new Board();
        Position p1 = new Position(5, 4);
        Position p2 = new Position(4, 5);
        Position p3 = new Position(3, 4);
        Position p4 = new Position(6, 3);
        Position p5 = new Position(5, 4);
        Position p6 = new Position(2, 1);
        Position p7 = new Position(3, 2);

        Move m1 = new Move(p1, p2);
        Move m2 = new Move(p2, p3);
        Move m3 = new Move(p4, p5);
        Move m4 = new Move(p6, p7);

        customBoard.makeMove(m1, Piece.Color.RED);
        customBoard.makeMove(m2, Piece.Color.RED);
        customBoard.makeMove(m3, Piece.Color.RED);
        customBoard.makeMove(m4, Piece.Color.WHITE);

        customBoard.getSpace(2, 7).updatePiece(new Piece(Piece.Type.KING, Piece.Color.WHITE));

        Game customGame1 = new Game(customBoard, redPlayerName, whitePlayerName, Piece.Color.WHITE, new ValidationHelper());
        customGame1.checkEndGame();
        assertFalse(customGame1.gameOver());
        assertTrue(customGame1.generateMovesList().size() > 0);
    }

    @Test
    public void checkEndGame2() {
        Board customBoard2 = new Board();
        Position p1 = new Position(2, 1);
        Position p2 = new Position(3, 2);
        Position p3 = new Position(4, 3);
        Position p4 = new Position(1, 0);
        Position p5 = new Position(2, 1);
        Position p6 = new Position(2, 5);
        Position p7 = new Position(3, 6);

        Move m1 = new Move(p1, p2);
        Move m2 = new Move(p2, p3);
        Move m3 = new Move(p4, p5);
        Move m4 = new Move(p6, p7);

        customBoard2.makeMove(m1, Piece.Color.WHITE);
        customBoard2.makeMove(m2, Piece.Color.WHITE);
        customBoard2.makeMove(m3, Piece.Color.WHITE);
        customBoard2.makeMove(m4, Piece.Color.RED);

        customBoard2.getSpace(5, 4).updatePiece(new Piece(Piece.Type.KING, Piece.Color.RED));

        Game customGame2 = new Game(customBoard2, redPlayerName, whitePlayerName, Piece.Color.RED, new ValidationHelper());
        customGame2.checkEndGame();
        assertFalse(customGame2.gameOver());
        assertTrue(customGame2.generateMovesList().size() > 0);
    }
}