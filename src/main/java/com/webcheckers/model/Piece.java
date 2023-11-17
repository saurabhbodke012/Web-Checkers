package com.webcheckers.model;

/**
 * The class represents the piece on the board
 */
public class Piece {
    /**
     * Defines the types of piece
     */
    public enum Type{
        SINGLE,
        KING
    }

    /**
     * Defines the colors of a piece
     */
    public enum Color {
        RED,
        WHITE
    }

    private Type type;
    private Color color;

    /**
     * The constructor for a piece
     * @param type - the type of piece
     * @param color - the color of the piece
     */
    public Piece(Type type, Color color){
        this.type = type;
        this.color = color;
    }

    /**
     * Getters
     */

    public Type getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }
}
// End of Class
