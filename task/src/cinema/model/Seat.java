package cinema.model;

public class Seat {

    private final int row;
    private final int col;
    private volatile boolean available;

    public Seat(int row, int col, boolean available) {
        this.row = row;
        this.col = col;
        this.available = available;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(final boolean available) {
        this.available = available;
    }
}
