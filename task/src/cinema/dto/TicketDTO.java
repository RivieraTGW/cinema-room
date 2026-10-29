package cinema.dto;

import cinema.util.SeatCoordinateConverter;
import cinema.util.SeatCoordinates;

public record TicketDTO(int row, int column, int price) {

    public TicketDTO(int row, int column, int price) {
        // Convert from internal 0-based index to client-facing 1-based index
        final SeatCoordinates clientCoordinates = SeatCoordinateConverter.toClientCoordinates(row, column);
        this.row = clientCoordinates.row();
        this.column = clientCoordinates.col();
        this.price = price;
    }
}
