package com.webcheckers.model;


import java.awt.geom.PathIterator;
import java.util.Iterator;

/**
 * Class represents a row on the game board
 * implemented as an array of spaces
 */
public class Row implements Iterable<Space> {

    // Declaring the array of Spaces and index as an Integer
    private Space[] spaces;
    private int index;

    /**
     * The constructor for a row.
     * Starts with a Light square first if the row is an even index and starts with
     * a dark square if the row is an odd index.
     * The constructor sets the type as Single and declares the Piece Color.
     * @param index
     * @param topColor
     */
    public Row(int index, Piece.Color topColor) {

        this.index = index;
        this.spaces = new Space [8];

        //Used to fill attributes
        Piece.Type startingType = Piece.Type.SINGLE;
        Piece.Color bottomColor;
        if (topColor == Piece.Color.WHITE){
            bottomColor = Piece.Color.RED;
        }else{
            bottomColor = Piece.Color.WHITE;
        }

        // Fill the row with spaces and pieces
        switch (index){
            case 0: //light-dark with top color pieces
                handleSpaces(Space.SPACECOLOR.LIGHT, Space.SPACECOLOR.DARK, null, new Piece(startingType, topColor));
                break;
            case 1://dark-light with top color pieces
                handleSpaces(Space.SPACECOLOR.DARK, Space.SPACECOLOR.LIGHT, new Piece(startingType, topColor),null);
                break;
            case 2: //light-dark with top color pieces
                handleSpaces(Space.SPACECOLOR.LIGHT, Space.SPACECOLOR.DARK, null, new Piece(startingType, topColor));
                break;
            case 3: //dark-light with no pieces
                handleSpaces(Space.SPACECOLOR.DARK, Space.SPACECOLOR.LIGHT, null,null);
                break;
            case 4: //light-dark with no pieces
                handleSpaces(Space.SPACECOLOR.LIGHT, Space.SPACECOLOR.DARK, null,null);
                break;
            case 5: //dark-light with bottom color pieces
                handleSpaces(Space.SPACECOLOR.DARK, Space.SPACECOLOR.LIGHT, new Piece(startingType, bottomColor),null);
                break;
            case 6: //light-dark with bottom color pieces
                handleSpaces(Space.SPACECOLOR.LIGHT, Space.SPACECOLOR.DARK,null, new Piece(startingType, bottomColor));
                break;
            case 7: //dark-light with bottom color pieces
                handleSpaces(Space.SPACECOLOR.DARK, Space.SPACECOLOR.LIGHT, new Piece(startingType, bottomColor),null);
                break;
        }
    }

    /**
     * initialize the spaces array for this row based on the parameters given
     * @param firstSpaceColor the color of the first space in this row
     * @param secondSpaceColor the color of the second space in this row
     * @param firstPiece the first piece in this row
     * @param secondPiece the second piece in this row
     * spaces after the first two will alternate using either the
     * first color and piece or the second
     */
    private void handleSpaces(Space.SPACECOLOR firstSpaceColor, Space.SPACECOLOR secondSpaceColor, Piece firstPiece, Piece secondPiece) {
        for (int i = 0; i < 8; i++) {
            // for even index
            if (i % 2 == 0) {
                spaces[i] = new Space(i, firstSpaceColor, firstPiece);
            }
            // for odd index
            else {
                spaces[i] = new Space(i, secondSpaceColor, secondPiece);
            }
        }
    }

    public void replaceSpace(int cell, Space space){
        spaces[cell] = space;
    }

    public Space getSpace(int index){
        return spaces[index];
    }

    public int getIndex() {
        return index; // return the index
    }

    /**
     * @return return an iterator that iterates over each space in the row
     */
    @Override
    public Iterator<Space> iterator() {
        return new SpaceIterator(spaces);
    }

    /**
     * @return return an iterator that iterates backwards over each space in the row
     */
    public Iterator<Space> reverseIterator(){return new ReverseSpaceIterator(spaces);}

    /**
     * A class to define how to iterate over an array of spaces.
     */
    public class SpaceIterator implements Iterator<Space> {
        // Declaring an Integer for index and array of Spaces
        private int index;
        private Space[] spaces;

        /**
         * A constructor for the iterator
         * @param spaces - the array of spaces in the row
         */
        public SpaceIterator(Space[] spaces) {
            // Declaring an Integer for index as 0 and setting the values of Spaces
            this.index = 0;
            this.spaces = spaces;
        }


        /**
         * Determines if the row has another space to iterate over
         * @return true:if there is another space left.
         */
        @Override
        public boolean hasNext() {
            return spaces.length >= index + 1;
        }

        /**
         * @return the next space in the iteration
         * increments the index
         */
        @Override
        public Space next() {
            Space nextSpace = spaces[index];
            index++;
            return nextSpace; // returning the nextSpace of type Space
        }

        /**
         * set the index (just for testing)
         * @param index the index
         */
        public void setIndex(int index) {
            this.index = index;
        }
    }

    /**
     * A class to define how to iterate over an array of spaces.
     */
    public class ReverseSpaceIterator implements Iterator<Space> {
        // Declaring an Integer for index and array of spaces
        private int index;
        private Space[] spaces;

        /**
         * A constructor for the iterator
         * @param spaces - the array of spaces in the row
         */
        public ReverseSpaceIterator(Space[] spaces) {
            // Declaring an Integer for index as 0 setting the values of spaces
            this.index = 7;
            this.spaces = spaces;
        }


        /**
         * Determines if the row has another space to iterate over
         * @return true if there is another space left.
         */
        @Override
        public boolean hasNext() {
            return index>=0;
        }

        /**
         * @return the next space in the iteration
         * increments the index
         */
        @Override
        public Space next() {
            Space nextSpace = spaces[index];
            index--;
            return nextSpace;
        }

        /**
         * set the index (just for testing)
         * @param index the index
         */
        public void setIndex(int index) {
            this.index = index;
        }
    }
}
// End of Class

