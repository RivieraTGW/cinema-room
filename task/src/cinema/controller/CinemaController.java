package cinema.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cinema.dto.SeatsResponse;
import cinema.service.CinemaService;

@RestController
public class CinemaController {

    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping("/seats")
    public ResponseEntity<SeatsResponse> getSeats() {
        final SeatsResponse seatsResponse = cinemaService.getAvailableSeats();
        return ResponseEntity.ok(seatsResponse);
    }

}
