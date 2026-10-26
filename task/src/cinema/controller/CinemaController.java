package cinema.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cinema.dto.ErrorResponse;
import cinema.dto.PurchaseRequest;
import cinema.dto.PurchaseResponse;
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

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseTicket(@RequestBody PurchaseRequest purchaseRequest) {
        final Map<String, String> result = new HashMap<>();
        if (cinemaService.purchaseTicket(result, purchaseRequest.getRow(), purchaseRequest.getColumn())) {
            // TO-DO: This smells. It depends on the fact that the map will contain this key.
            final int price = Integer.parseInt(result.get("price"));
            final PurchaseResponse purchaseResponse = new PurchaseResponse(purchaseRequest.getRow(), purchaseRequest.getColumn(), price);
            return ResponseEntity.ok(purchaseResponse);
        }

        final String error = result.getOrDefault("error", "");
        final ErrorResponse errorResponse = new ErrorResponse(error);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
