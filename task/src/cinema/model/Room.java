package cinema.model;

import cinema.exception.ErrorMessages;

public class Room {

    private final int numRows;
    private final int numCols;
    private final Seat[][] seats;

    public Room(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.seats = new Seat[this.numRows][this.numCols];

        initializeSeats();
    }

    private void initializeSeats() {
        for (int row = 0; row < this.numRows; row++) {
            for (int col = 0; col < this.numCols; col++) {
                seats[row][col] = new Seat(row, col, true);
            }
        }
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public Seat getSeat(final int row, final int col) {
        if (row < 0 || row >= this.numRows || col < 0 || col >= this.numCols) {
            throw new IllegalArgumentException(ErrorMessages.ROW_COL_OUT_OF_BOUNDS_ERR);
        }

        return seats[row][col];
    }
}
