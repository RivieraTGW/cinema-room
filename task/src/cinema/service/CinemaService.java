package cinema.service;

import java.util.UUID;

import cinema.dto.PurchaseResponse;
import cinema.dto.ReturnResponse;
import cinema.dto.SeatsResponse;

public interface CinemaService {

    /**
     * Returns a list of available seats.
     */
    SeatsResponse getAvailableSeats();

    /**
     * Purchases a ticket at the specified seat.
     * This method is thread-safe: only one purchase can be processed at a time.
     */
    PurchaseResponse purchaseTicket(int row, int col);

    /**
     * Refunds a ticket using the given purchase token.
     * This method is thread-safe: only one refund can be processed at a time.
     */
    ReturnResponse returnTicket(UUID token);

}
