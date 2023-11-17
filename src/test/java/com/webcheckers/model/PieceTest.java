package com.webcheckers.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {
    // Initiating the Object

    // initiating the object


    final Piece CuT = new Piece(Piece.Type.SINGLE, Piece.Color.RED);

    // test for getType()
    @Test
    public void getType(){

        assertEquals(Piece.Type.SINGLE,CuT.getType()); // checks if equal

    }

    // test for getColor()
    @Test
    public void getColor()
    {
        assertEquals(Piece.Color.RED,CuT.getColor());
    }

    // test for getColor()
    @Test
    public void getColor_1(){

        assertNotEquals(Piece.Color.WHITE,CuT.getColor());
    }

}