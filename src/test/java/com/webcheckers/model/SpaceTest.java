package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import com.webcheckers.model.Space.SPACECOLOR;


/**
 * The unit test suite for the {@link Space} component.
 *
 * @author <a href='mailto:pjw7904@rit.edu'>Peter Willis</a>
 */
@Tag("Model-tier")
public class SpaceTest {
    // constant for a random, valid cell index
    private static final int VALID_CELL_IDX = 5;
    private Space space;

    @BeforeEach
    void setup() {
        space = new Space(0, SPACECOLOR.DARK);
        space.updatePiece(new Piece(Piece.Type.SINGLE, Piece.Color.RED));
    }

    /**
     * Test that the parameterized constructor that includes a piece works without failure.
     */
    @Test
    public void ctor_args_withPiece() {
        // Use a mock piece with the parameterized constructor call
        final Piece piece = mock(Piece.class);

        // Test for both light and dark spaces
        new Space(VALID_CELL_IDX, SPACECOLOR.DARK, piece);
        new Space(VALID_CELL_IDX, SPACECOLOR.LIGHT, piece);
    }

    /**
     * Test that the parameterized constructor that does NOT include a piece works without failure.
     */
    @Test
    public void ctor_args_withoutPiece() {
        // LIGHT or DARK color does not matter, it just has to be a valid value
        final Space CuT = new Space(VALID_CELL_IDX, SPACECOLOR.DARK);

        // Make sure that the CuT does not have a valid piece
        assertNull(CuT.getPiece());
    }

    /**
     * Test the {@link Space#isValid()} method.
     */
    @Test
    public void test_isValid() {
        // Use a mock piece with the parameterized constructor call
        final Piece piece = mock(Piece.class);

        // Instantiate a Space with a piece and a dark color
        final Space CuTPieceDark = new Space(VALID_CELL_IDX, SPACECOLOR.DARK, piece);

        // Instantiate a Space with no piece and a dark color
        final Space CuTNoPiece = new Space(VALID_CELL_IDX, SPACECOLOR.DARK);

        // Instantiate a Space with no piece and a light color
        final Space CutPieceLight = new Space(VALID_CELL_IDX, SPACECOLOR.LIGHT);

        assertFalse(CuTPieceDark.isValid());
        assertTrue(CuTNoPiece.isValid());
        assertFalse(CutPieceLight.isValid());
    }

    /**
     * Test the {@link Space#getPiece()} method.
     */
    @Test
    public void test_getPiece() {
        // Use a mock piece with the parameterized constructor call
        final Piece piece = mock(Piece.class);
        final Space CuT = new Space(VALID_CELL_IDX, SPACECOLOR.DARK, piece);

        // Invoke test: piece given to constructor matches piece object
        assertEquals(piece, CuT.getPiece());
    }

    /**
     * Test the {@link Space#getCellIdx()} method.
     */
    @Test
    public void test_getCellIdx() {
        final Space CuT = new Space(VALID_CELL_IDX, SPACECOLOR.DARK);

        // Invoke test: idx given to constructor matches the value
        assertSame(VALID_CELL_IDX, CuT.getCellIdx());
    }

    @Test
    public void updatePiece() {
        assertEquals(Piece.Color.RED, space.getPiece().getColor());
    }

    @Test
    public void removePiece() {
        space.removePiece();
        assertNull(space.getPiece());
    }

    @Test
    public void getSpaceColor() {
        assertEquals(SPACECOLOR.DARK, space.getSpaceColor());
    }
}