package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class RowTest {
    private Row rowWhite;
    private Row rowRed;
    private Row invalidIndex;

    @BeforeEach
    void setUp() {
        rowWhite = new Row(0, Piece.Color.WHITE);
        rowRed = new Row(7, Piece.Color.RED);
        invalidIndex = new Row(9, Piece.Color.RED);
    }

    @Test
    void getIndex() {
        assertEquals(0, rowWhite.getIndex());
        assertEquals(9, invalidIndex.getIndex());
    }

    @Test
    void iterator() {
        assertEquals("SpaceIterator", rowRed.iterator().getClass().getSimpleName());
    }

    @Test
    void reverseIterator() {
        assertEquals("ReverseSpaceIterator", rowRed.reverseIterator().getClass().getSimpleName());
    }

    @Nested
    class SpaceIteratorTest {
        @Test
        void next() {
            assertNotNull(rowWhite.iterator().next());
        }

        @Test
        void hasNext() {
            assertTrue(rowWhite.iterator().hasNext());
            Row.SpaceIterator iterator = (Row.SpaceIterator)rowWhite.iterator();
            iterator.setIndex(8);
            assertFalse(iterator.hasNext());
        }
    }

    @Nested
    class ReverseSpaceIteratorTest {
        @Test
        void next() {
            assertNotNull(rowWhite.reverseIterator().next());
        }

        @Test
        void hasNext() {
            assertTrue(rowWhite.reverseIterator().hasNext());
            Row.ReverseSpaceIterator iterator = (Row.ReverseSpaceIterator)rowWhite.reverseIterator();
            iterator.setIndex(-1);
            assertFalse(iterator.hasNext());
        }
    }
}