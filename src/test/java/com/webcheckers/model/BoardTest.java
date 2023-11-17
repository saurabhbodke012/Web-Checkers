package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;
    private Position pos1;
    private Position pos2;
    private Move kingingWhite;
    private Move kingingRed;
    private Position pos5;
    private Position pos6;

    @BeforeEach
    void setUp() {
        board = new Board();
        pos1 = new Position(6, 1);
        pos2 = new Position(7, 2);
        kingingWhite = new Move(pos1, pos2);

        pos5 = new Position(1, 0);
        pos6 = new Position(0, 1);
        kingingRed = new Move(pos5, pos6);
    }

    @Test
    void getSpace() {
        assertEquals("Space", board.getSpace(2, 2).getClass().getSimpleName());

        //8 branches to test
        assertNull(board.getSpace(-5, -5));
        assertNull(board.getSpace(10, 10));
        assertNull(board.getSpace(-5, 10));
        assertNull(board.getSpace(10, -5));
        assertNull(board.getSpace(3, -5));
        assertNull(board.getSpace(5, -3));
        assertNull(board.getSpace(10, 5));
        assertNull(board.getSpace(5, 10));
        assertNull(board.getSpace(-5, 5));
        assertNull(board.getSpace(5, -5));
    }

    @Test
    void getRow() {
        assertEquals("Row", board.getRow(0).getClass().getSimpleName());
    }

    @Test
    void makeMove() {
        //check that kinging works
        board.makeMove(kingingWhite, Piece.Color.WHITE);
        assertEquals(Piece.Type.KING, board.getRow(kingingWhite.getEnd().getRow()).getSpace(kingingWhite.getEnd().getCell()).getPiece().getType());

        //check that king movement works
        Position pos3 = new Position(6, 3);
        board.makeMove(new Move(pos2, pos3), Piece.Color.WHITE);
        assertEquals(Piece.Type.KING, board.getRow(6).getSpace(3).getPiece().getType());

        //check that single movement works
        board.makeMove(new Move(new Position(5, 0), new Position(4, 1)), Piece.Color.WHITE);
        assertEquals(Piece.Type.SINGLE, board.getRow(4).getSpace(1).getPiece().getType());
        //check that capture works
        board.makeMove(new Move(new Position(4, 1), new Position(2, 3)), Piece.Color.WHITE);
        assertNull(board.getRow(3).getSpace(2).getPiece());

        //king a red piece for branch coverage
        board.makeMove(kingingRed, Piece.Color.RED);
        assertEquals(Piece.Type.KING, board.getRow(kingingRed.getEnd().getRow()).getSpace(kingingRed.getEnd().getCell()).getPiece().getType());
    }

    @Test
    void iterator() {
        assertEquals("RowIterator", board.iterator().getClass().getSimpleName());
    }

    @Test
    void reverseIterator() {
        assertEquals("ReverseRowIterator", board.reverseIterator().getClass().getSimpleName());
    }

    @Nested
    class RowIteratorTest {
        @Test
        void next() {
            assertNotNull(board.iterator().next());
        }

        @Test
        void hasNext() {
            assertTrue(board.iterator().hasNext());
            Board.RowIterator iterator = (Board.RowIterator)board.iterator();
            iterator.setIndex(8);
            assertFalse(iterator.hasNext());
        }
    }

    @Nested
    class ReverseRowIteratorTest {
        @Test
        void next() {
            assertNotNull(board.reverseIterator().next());
        }

        @Test
        void hasNext() {
            assertTrue(board.iterator().hasNext());
            Board.ReverseRowIterator iterator = (Board.ReverseRowIterator)board.reverseIterator();
            iterator.setIndex(-1);
            assertFalse(iterator.hasNext());
            iterator.setIndex(0);
            assertTrue(iterator.hasNext());
        }
    }
}