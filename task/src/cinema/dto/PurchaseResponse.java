package cinema.dto;

public record PurchaseResponse(int row, int column, int price) {

    public PurchaseResponse(int row, int column, int price) {
        // Convert from internal 0-based index to client-facing 1-based index
        this.row = row + 1;
        this.column = column + 1;
        this.price = price;
    }

}
