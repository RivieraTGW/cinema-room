package cinema.model;

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

    public Seat getSeat(int row, int col) {
        if (row < 0 || row >= this.numRows || col < 0 || col >= this.numCols) {
            throw new IllegalArgumentException("Seat (" + row + ", " + col + ") is out of bounds. " +
                    "Valid range: rows 0-" + (this.numRows-1) + ", columns 0-" + (this.numCols-1) + ".");
        }

        return seats[row][col];
    }
}
