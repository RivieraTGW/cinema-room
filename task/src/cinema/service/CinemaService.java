package cinema.service;

import cinema.dto.PurchaseRequest;
import cinema.dto.PurchaseResponse;
import cinema.dto.ReturnRequest;
import cinema.dto.SeatsResponse;
import cinema.dto.StatsResponse;
import cinema.dto.TicketDTO;

public interface CinemaService {

    /**
     * Returns a list of available seats.
     */
    SeatsResponse getAvailableSeats();

    /**
     * Purchases a ticket at the specified seat.
     * Only one purchase can be processed at a time.
     */
    PurchaseResponse purchaseTicket(PurchaseRequest purchaseRequest);

    /**
     * Refunds a ticket using the given purchase token.
     * Only one refund can be processed at a time.
     */
    TicketDTO returnTicket(ReturnRequest returnRequest);

    /**
     * Returns cinema statistics if the password is correct.
     */
    StatsResponse getStatistics(String password);
}
