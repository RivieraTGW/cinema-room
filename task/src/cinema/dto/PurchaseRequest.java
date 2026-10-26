package cinema.dto;

public class PurchaseRequest {

    private int row;
    private int column;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        // Convert from client-facing 1-based index to internal 0-based index
        this.row = row - 1;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        // Convert from client-facing 1-based index to internal 0-based index
        this.column = column - 1;
    }

}
