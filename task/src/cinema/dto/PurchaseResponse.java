package cinema.dto;

import java.util.UUID;

import cinema.util.SeatCoordinateConverter;
import cinema.util.SeatCoordinates;

public record PurchaseResponse(UUID token, TicketDTO ticket) {

    public PurchaseResponse(UUID token, int row, int column, int price) {
        // Note: if this was a bigger project it might have been worthwhile to add a separate mapper class
        this(token, new TicketDTO(row, column, price));
    }
}

record TicketDTO(int row, int column, int price) {

    public TicketDTO(int row, int column, int price) {
        // Convert from internal 0-based index to client-facing 1-based index
        final SeatCoordinates clientCoordinates = SeatCoordinateConverter.toClientCoordinates(row, column);
        this.row = clientCoordinates.row();
        this.column = clientCoordinates.col();
        this.price = price;
    }
}
