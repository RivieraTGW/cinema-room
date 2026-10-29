package cinema.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import cinema.dto.PurchaseRequest;
import cinema.dto.PurchaseResponse;
import cinema.dto.ReturnResponse;
import cinema.dto.ReturnRequest;
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
    public ResponseEntity<PurchaseResponse> purchaseTicket(@RequestBody @NonNull final PurchaseRequest purchaseRequest) {
        final PurchaseResponse purchaseResponse = cinemaService.purchaseTicket(
                purchaseRequest.row(),
                purchaseRequest.column());
        return ResponseEntity.ok(purchaseResponse);
    }

    @PostMapping("/return")
    public ResponseEntity<ReturnResponse> returnTicket(@RequestBody @NonNull final ReturnRequest returnRequest) {
        final ReturnResponse returnResponse = cinemaService.returnTicket(returnRequest.token());
        return ResponseEntity.ok(returnResponse);
    }

}
