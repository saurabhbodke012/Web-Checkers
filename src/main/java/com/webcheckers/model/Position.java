package com.webcheckers.model;

import java.util.Objects;

public class Position {
    private int row;
    private int cell;

    //
    // Constructor
    //

    public Position(int row, int cell) {
        this.row = row;
        this.cell = cell;
    }


    //
    // Public methods
    //

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Position))
            return false;
        Position otherP = (Position)o;
        return this.row == otherP.getRow() && this.cell == otherP.getCell();
    }

    @Override

    public String toString() {
        return String.format("row = %d | cell = %d\n", row, cell);
    }
}
