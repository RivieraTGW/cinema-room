package cinema.service;

import cinema.dto.PurchaseResponse;
import cinema.dto.SeatsResponse;

public interface CinemaService {
    SeatsResponse getAvailableSeats();
    PurchaseResponse purchaseTicket(int row, int col);
}
