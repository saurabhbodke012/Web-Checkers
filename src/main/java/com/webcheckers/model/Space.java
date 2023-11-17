package com.webcheckers.model;

/**
 * The class represents the space on the checkers board
 */
public class Space {
    /**
     * Defines the color of the space
     */
    public enum SPACECOLOR{
        LIGHT, // light color
        DARK // dark color
    }

    //Attributes
    private int cellIdx;
    private SPACECOLOR spaceColor;
    private Piece piece;

    /**
     * Constructor for the space without a piece on it.
     * @param cellIdx - the index of the cell
     * @param spaceColor - the color of the space
     */
    public Space(int cellIdx, SPACECOLOR spaceColor){
        // Setting the value of Celldx, and Spacecolor
        this.cellIdx = cellIdx;
        this.spaceColor = spaceColor;
    }

    /**
     * Constructor for the space with a piece on it
     * @param cellIdx - the index of the cell
     * @param spaceColor - the color of the space
     */
    public Space(int cellIdx, SPACECOLOR spaceColor, Piece piece){
        // Setting the value of Celldx, Piece and Space color
        this.cellIdx = cellIdx;
        this.piece = piece;
        this.spaceColor = spaceColor;
    }

    /**
     * If the piece is not occupied and the space color is dark,
     * the space is valid/open for a piece.
     * @return True if valid, false if invalid
     */
    public boolean isValid(){
        Boolean temp = piece == null && spaceColor == SPACECOLOR.DARK;
        return temp;
    }

    /**
     * @return the piece on the board if one exists
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Set a new piece down on this space
     * @param pieceBeingDropped the piece to drop on this space
     */
    public void updatePiece(Piece pieceBeingDropped) {
        piece = pieceBeingDropped;
    }

    /**
     * Move the piece off of this space
     */
    public void removePiece() {
        piece = null;
    }


    /**
     * @return the index of the space
     */
    public int getCellIdx() {

        return cellIdx;
    }

    /**
     * @return The color of the Space
     */
    public SPACECOLOR getSpaceColor() {
        return spaceColor;
    }
}
