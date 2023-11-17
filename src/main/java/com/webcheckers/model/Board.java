package com.webcheckers.model;
import java.util.Iterator;


/**
 * The Class Stores all the rows and cells on the board
 * implemented as an array of rows
 */
public class Board implements Iterable<Row>{
    // initialize the array of Row
    private Row[] rows;

    /**
     * Constructor for Class Board and setting the white color as top color
     * and initialize the array of row with size 8
     */
    public Board(){
        Piece.Color topColor = Piece.Color.WHITE;
        rows = new Row [8];
        for(int i=0; i<8; i++){
            rows[i] = new Row(i, topColor);
        }
    }


    public Space getSpace(int row, int col){
        // method -> returning Space type
        if (0 <= row && row <= 7 && 0<=col &&col<=7){
            return rows[row].getSpace(col); // return the space
        }
        return null; // return null
        }
    public Row getRow(int index) {
        return rows[index];
    }

    public void makeMove(Move move, Piece.Color activeColor){
        Position start = move.getStart();
        Position end = move.getEnd();
        Space startSpace = getSpace(start.getRow(), start.getCell());
        Piece startPiece = startSpace.getPiece();

        rows[start.getRow()].replaceSpace(start.getCell(), new Space(start.getCell(), Space.SPACECOLOR.DARK, null));

        if (startPiece.getType() == Piece.Type.SINGLE && (end.getRow() == 0 || end.getRow() == 7)){
            rows[end.getRow()].replaceSpace(end.getCell(), new Space(end.getCell(), Space.SPACECOLOR.DARK, new Piece(Piece.Type.KING, activeColor)));
        }else if (startPiece.getType() == Piece.Type.KING){
            rows[end.getRow()].replaceSpace(end.getCell(), new Space(end.getCell(), Space.SPACECOLOR.DARK, new Piece(Piece.Type.KING, activeColor)));
        }else{
            rows[end.getRow()].replaceSpace(end.getCell(), new Space(end.getCell(), Space.SPACECOLOR.DARK, new Piece(Piece.Type.SINGLE, activeColor)));
        }

        if (Math.abs(start.getRow() - end.getRow()) == 2){
            int centerRow = (start.getRow() + end.getRow())/2;
            int centerCol = (start.getCell() + end.getCell())/2;
            rows[centerRow].replaceSpace(centerCol, new Space(centerCol, Space.SPACECOLOR.DARK, null)); // updating
        }
    }

    /**
     * @return return an iterator that iterates over each row on the board
     */
    @Override
    public Iterator<Row> iterator(){
        return new RowIterator(rows);
    }

    /**
     * @return return an iterator that iterates backwards over each row on the board
     */
    public Iterator<Row> reverseIterator(){
        return new ReverseRowIterator(rows);
    }

    /**
     * A class to define how to iterate over an array of spaces.
     */
    public class RowIterator implements Iterator<Row>{
        // Declaring an Integer for index and array of row
        private  int index;
        private Row[] rows;

        /**
         * A constructor for the iterator
         * @param rows - the array of rows on the board
         */
        public RowIterator(Row[] rows){
            // initializing the values for index and array of row
            this.index = 0;
            this.rows = rows;
        }

        /**
         * Determines if the board has another row to iterate over
         * @return true:if there is another row left.
         */
        @Override
        public boolean hasNext(){
            return rows.length >= index +1; // checks if the length is greater or equal to index value or index+1 value
        }

        /**
         * @return the next row in the iteration
         * increments the index
         */
        @Override
        public Row next(){
            Row nextRow = rows[index]; // declaring and initialising the value for variable nextRow
            index++; // incrementing the index
            return nextRow; // returning the variable
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
    public class ReverseRowIterator implements Iterator<Row>{
        // Declaring an Integer for index and array of row
        private  int index;
        private Row[] rows;

        /**
         * A constructor for the iterator
         * @param rows - the array of rows on the board
         */
        public ReverseRowIterator(Row[] rows){
            this.index = 7;
            this.rows = rows;
        }

        /**
         * Determines if the board has another row to iterate over
         * and checks if the index is greater or equal to 0
         * @return true:if there is another row left.
         */
        @Override
        public boolean hasNext(){
            return this.index >= 0;
        }

        /**
         * @return the next row in the iteration
         * increments the index
         */
        @Override
        public Row next(){
            Row nextRow = rows[index];
            index--;
            return nextRow;
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
