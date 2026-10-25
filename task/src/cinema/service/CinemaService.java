package cinema.service;

import cinema.dto.SeatsResponse;

public interface CinemaService {
    SeatsResponse getAvailableSeats();
}
