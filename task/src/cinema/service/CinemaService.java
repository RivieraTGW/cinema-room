package cinema.service;

import java.util.UUID;

import cinema.dto.*;

public interface CinemaService {

    /**
     * Returns a list of available seats.
     * This method is thread-safe.
     */
    SeatsResponse getAvailableSeats();

    /**
     * Purchases a ticket at the specified seat.
     * This method is thread-safe: only one purchase can be processed at a time.
     */
    PurchaseResponse purchaseTicket(PurchaseRequest purchaseRequest);

    /**
     * Refunds a ticket using the given purchase token.
     * This method is thread-safe: only one refund can be processed at a time.
     */
    TicketDTO returnTicket(ReturnRequest returnRequest);

}
