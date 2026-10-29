package cinema.dto;

import java.util.UUID;

public record PurchaseResponse(UUID token, TicketDTO ticket) {

    public PurchaseResponse(UUID token, int row, int column, int price) {
        // Note: if this was a bigger project it might have been worthwhile to add a separate mapper class
        this(token, new TicketDTO(row, column, price));
    }
}
