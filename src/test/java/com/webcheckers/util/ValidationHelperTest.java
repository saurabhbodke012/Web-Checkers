package com.webcheckers.util;

import com.webcheckers.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidationHelperTest {
    private ValidationHelper validationHelper;
    private Position start;
    private Position end;
    private Position mid;
    private Space space;
    private Board board;
    private Board concBoard;
    private Piece piece;
    private ArrayList<Move> possibleMoves;
    private ArrayList<ArrayList<Move>> possibleJumps;

    @BeforeEach
    void setUp() {
        validationHelper = new ValidationHelper();
        start = mock(Position.class);
        end = mock(Position.class);
        mid = mock(Position.class);
        board = mock(Board.class);
        concBoard = new Board();
        space = mock(Space.class);
        piece = mock(Piece.class);
        possibleMoves = new ArrayList<>();
        possibleJumps = new ArrayList<>();
    }

    @Test
    void piecesOfColor() {
        assertEquals(12, validationHelper.piecesOfColor(concBoard, Piece.Color.RED));
        assertEquals(12, validationHelper.piecesOfColor(concBoard, Piece.Color.WHITE));
    }

    @Test
    void scanBoard() {
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

        concBoard.makeMove(m1, Piece.Color.RED);
        concBoard.makeMove(m2, Piece.Color.RED);
        concBoard.makeMove(m3, Piece.Color.RED);
        concBoard.makeMove(m4, Piece.Color.WHITE);

        concBoard.getSpace(2, 7).updatePiece(new Piece(Piece.Type.KING, Piece.Color.WHITE));

        validationHelper.scanBoard(concBoard, Piece.Color.WHITE, possibleMoves, possibleJumps);


        assertTrue(possibleMoves.size() > 0);
        assertTrue(possibleJumps.size() > 0);

        ArrayList<Move> possibleMoves2 = new ArrayList<>();
        ArrayList<ArrayList<Move>> possibleJumps2 = new ArrayList<>();
        validationHelper.scanBoard(concBoard, Piece.Color.RED, possibleMoves2, possibleJumps2);

        assertTrue(possibleMoves2.size() > 0);
        assertFalse(possibleJumps2.size() > 0);
    }

    @Test
    void scanBoard2() {
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

        concBoard.makeMove(m1, Piece.Color.WHITE);
        concBoard.makeMove(m2, Piece.Color.WHITE);
        concBoard.makeMove(m3, Piece.Color.WHITE);
        concBoard.makeMove(m4, Piece.Color.RED);

        concBoard.getSpace(5, 4).updatePiece(new Piece(Piece.Type.KING, Piece.Color.RED));

        validationHelper.scanBoard(concBoard, Piece.Color.RED, possibleMoves, possibleJumps);


        assertTrue(possibleMoves.size() > 0);
        assertTrue(possibleJumps.size() > 0);

        ArrayList<Move> possibleMoves2 = new ArrayList<>();
        ArrayList<ArrayList<Move>> possibleJumps2 = new ArrayList<>();
        validationHelper.scanBoard(concBoard, Piece.Color.WHITE, possibleMoves2, possibleJumps2);

        assertTrue(possibleMoves2.size() > 0);
        assertTrue(possibleJumps2.size() > 0);
    }

    @Test
    void checkAndAddNormalMove() {

        assertTrue(validationHelper.checkAndAddNormalMove(start, new Position(9,9), board, new ArrayList<Move>()));
        when(end.getRow()).thenReturn(5);
        when(board.getSpace(end.getRow(), end.getCell())).thenReturn(space);
        when(space.getPiece()).thenReturn(piece);


        assertFalse(validationHelper.checkAndAddNormalMove(new Position(4, 3), end, board, new ArrayList<Move>()));
        when(end.getRow()).thenReturn(0);

        assertTrue(validationHelper.checkAndAddNormalMove(new Position(4, 3), end, new Board(), new ArrayList<Move>()));
        when(end.getRow()).thenReturn(7);


        assertFalse(validationHelper.checkAndAddNormalMove(new Position(4, 3), end, new Board(), new ArrayList<Move>()));

        when(end.getRow()).thenReturn(10);
        assertTrue(validationHelper.checkAndAddNormalMove(new Position(4, 3), end, new Board(), new ArrayList<Move>()));

        when(end.getCell()).thenReturn(-6);
        assertTrue(validationHelper.checkAndAddNormalMove(new Position(4, 3), end, new Board(), new ArrayList<Move>()));

        when(end.getCell()).thenReturn(0);
        assertTrue(validationHelper.checkAndAddNormalMove(new Position(4, 3), end, new Board(), new ArrayList<Move>()));

        when(end.getCell()).thenReturn(5);
        assertTrue(validationHelper.checkAndAddNormalMove(new Position(4, 3), end, new Board(), new ArrayList<Move>()));

        when(end.getCell()).thenReturn(7);
        assertTrue(validationHelper.checkAndAddNormalMove(new Position(4, 3), end, new Board(), new ArrayList<Move>()));

        when(end.getCell()).thenReturn(10);
        assertTrue(validationHelper.checkAndAddNormalMove(new Position(4, 3), end, new Board(), new ArrayList<Move>()));

        when(end.getRow()).thenReturn(-6);
        assertTrue(validationHelper.checkAndAddNormalMove(new Position(4, 3), end, new Board(), new ArrayList<Move>()));
    }

    @Test
    void checkValidJump() {
        when(mid.getRow()).thenReturn(3);
        when(mid.getCell()).thenReturn(2);
        when(piece.getColor()).thenReturn(Piece.Color.WHITE);

        when(end.getRow()).thenReturn(-9);

        when(board.getSpace(end.getRow(), end.getCell())).thenReturn(space);
        when(space.getPiece()).thenReturn(piece);

        assertNull(validationHelper.checkValidJump(start, mid, end, new Board(), Piece.Color.RED));

        Board board2 = new Board();
        Move move1 = new Move(new Position(5, 4), new Position(4, 5));
        Move move2 = new Move(new Position(4, 5), new Position(3, 4));
        board2.makeMove(move1, Piece.Color.RED);
        board2.makeMove(move2, Piece.Color.RED);

        assertNotNull(validationHelper.checkValidJump(new Position(2, 5), new Position(3, 4), new Position(4, 3), board2, Piece.Color.WHITE));

    }
}