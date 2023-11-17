package com.webcheckers.model;

import java.util.Objects;

public class Move {


    public static int JUMP_DISTANCE;

    //
    // Attributes
    //

    private Position start;
    private Position end;

    //
    // Constructor
    //

    public Move(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    //
    // Public methods
    //

    public Position getStart() {
        return start;
    }

    public void setStart(Position start) {
        this.start = start;
    }

    public Position getEnd() {
        return end;
    }

    public void setEnd(Position end) {
        this.end = end;
    }

    @Override
    public String toString() {
        String move = " ";
        move += " Start(Row : " + start.getRow() + " Cell : " + start.getCell() + ") ";
        move += "End(Row : " + end.getRow() + "Cell : " + end.getCell() + ") ";
        return move;
    }

    public String altString(Piece.Color color){
        Position s;
        Position e;
        if (color == Piece.Color.RED){
            s = new Position(7-start.getRow(),start.getCell());
            e = new Position(7-end.getRow(), end.getCell());
        } else{
            s = new Position(start.getRow(), 7- start.getCell());
            e = new Position(end.getRow(), 7- end.getCell());
        }
        return s + " to " + e;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Move))
            return false;
        Move otherM = (Move)o;
        return this.start.equals(otherM.getStart()) && this.end.equals(otherM.getEnd());
    }

    public boolean isJump() {
        int rowIndexMovementDistance = Math.abs(start.getRow() - end.getRow());
        int spaceIndexMovementDistance = Math.abs(start.getCell() - end.getCell());

        return (rowIndexMovementDistance == JUMP_DISTANCE && spaceIndexMovementDistance == JUMP_DISTANCE);
    }

}
