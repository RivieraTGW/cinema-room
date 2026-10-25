package cinema.dto;

public record SeatDTO(int row, int column) {

    public SeatDTO(int row, int column) {
        // Convert from internal 0-based index to client-facing 1-based index
        this.row = row + 1;
        this.column = column + 1;
    }
}
