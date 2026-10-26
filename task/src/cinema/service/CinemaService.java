package cinema.service;

import java.util.Map;

import cinema.dto.SeatsResponse;

public interface CinemaService {
    SeatsResponse getAvailableSeats();
    boolean purchaseTicket(Map<String, String> result, int row, int col);
}
