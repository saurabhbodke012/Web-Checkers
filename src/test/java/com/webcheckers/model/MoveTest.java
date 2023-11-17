package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {
    private Position p1;
    private Position p2;
    private Position p3;
    private Move move;
    private Move jump;

    @BeforeEach
    void setUp() {
        p1 = new Position(3, 4);
        p2 = new Position(4, 5);
        p3 = new Position(5, 6);

        move = new Move(p1, p2);
        jump = new Move(p1, p3);
    }

    @Test
    void setStart() {
        move.setStart(new Position(5, 6));
        assertEquals(new Position(5, 6), move.getStart());
    }

    @Test
    void setEnd() {
        move.setEnd(new Position(5, 6));
        assertEquals(new Position(5, 6), move.getEnd());
    }

    @Test
    void altString() {
        assertEquals("row = 4 | cell = 4\n" +
                " to row = 3 | cell = 5\n", move.altString(Piece.Color.RED));

        assertEquals("row = 3 | cell = 3\n" +
                " to row = 4 | cell = 2\n"
                , move.altString(Piece.Color.WHITE));
    }

    @Test
    void testEquals() {
        assertTrue(move.equals(new Move(p1, p2)));
        assertFalse(move.equals(null));
        assertFalse(move.equals(jump));

        Move forBranchCov = new Move(p1, new Position(3, 9));
        Move forBranchCov2 = new Move(new Position(0, 0), new Position(3, 9));
        assertFalse(move.equals(forBranchCov));
        assertFalse(move.equals(forBranchCov2));
    }

    @Test
    void isJump() {
        Move.JUMP_DISTANCE = 2;
        assertTrue(jump.isJump());
        assertFalse(move.isJump());

        Move forBranchCov = new Move(p1, new Position(3, 9));
        Move forBranchCov2 = new Move(new Position(0, 0), new Position(3, 9));
        Move forBranchCov3 = new Move(new Position(0, 0), new Position(2, 3));

        assertFalse(forBranchCov.isJump());
        assertFalse(forBranchCov2.isJump());
        assertFalse(forBranchCov3.isJump());
    }
}