package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    private Position p;

    @BeforeEach
    void setUp() {
        p = new Position(3, 4);
    }

    @Test
    void setRow() {
        p.setRow(4);
        assertEquals(4, p.getRow());
    }

    @Test
    void setCell() {
        p.setCell(6);
        assertEquals(6, p.getCell());
    }

    @Test
    void testEquals() {
        assertFalse(p.equals(null));
        assertFalse(p.equals(new Position(3, 5)));
    }
}