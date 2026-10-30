package cinema.model;

import cinema.exception.ErrorMessages;

public class Room {

    private final int numRows;
    private final int numCols;
    private final Seat[][] seats;

    public Room(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;

        // Since client-facing seat indices are 1-based, add one extra
        // row and column, so that we don't have to convert between
        // the 1-based system and 0-based system.
        this.seats = new Seat[this.numRows + 1][this.numCols + 1];
        initializeSeats();
    }

    private void initializeSeats() {
        for (int row = 0; row <= this.numRows; row++) {
            for (int col = 0; col <= this.numCols; col++) {
                if (row == 0 || col == 0) {
                    // These seats must always be unavailable, because they don't
                    // represent actual seats. Seat indices start from 1.
                    seats[row][col] = new Seat(row, col, false);
                } else {
                    seats[row][col] = new Seat(row, col, true);
                }
            }
        }
    }

    public Seat getSeat(final int row, final int col) {
        if (row < 1 || row > this.numRows || col < 1 || col > this.numCols) {
            throw new IllegalArgumentException(ErrorMessages.ROW_COL_OUT_OF_BOUNDS_ERR);
        }

        return seats[row][col];
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }
}
