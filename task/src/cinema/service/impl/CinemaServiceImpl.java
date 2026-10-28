package cinema.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import cinema.dto.PurchaseResponse;
import cinema.dto.SeatDTO;
import cinema.dto.SeatsResponse;
import cinema.exception.ErrorMessages;
import cinema.exception.SeatAlreadyPurchasedException;
import cinema.repository.CinemaRepository;
import cinema.service.CinemaService;
import cinema.util.SeatCoordinates;

@Service
public class CinemaServiceImpl implements CinemaService {

    private final CinemaRepository cinemaRepository;

    public CinemaServiceImpl(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public SeatsResponse getAvailableSeats() {
        final List<SeatCoordinates> availableSeats = cinemaRepository.getAvailableSeats();
        final List<SeatDTO> availableSeatsDTO = new ArrayList<>();

        for (final SeatCoordinates seatCoordinates : availableSeats) {
            availableSeatsDTO.add(new SeatDTO(
                    seatCoordinates.row(),
                    seatCoordinates.col(),
                    cinemaRepository.getTicketPrice(seatCoordinates.row())));
        }

        return new SeatsResponse(cinemaRepository.getNumRows(), cinemaRepository.getNumCols(), availableSeatsDTO);
    }

    @Override
    public synchronized PurchaseResponse purchaseTicket(final int row, final int col) {
        if (!cinemaRepository.isSeatAvailable(row, col)) {
            throw new SeatAlreadyPurchasedException(ErrorMessages.SEAT_ALREADY_PURCHASED_ERR);
        }

        cinemaRepository.setSeatAvailable(row, col, false);
        final int price = cinemaRepository.getTicketPrice(row);
        final UUID token = UUID.randomUUID();
        cinemaRepository.savePurchase(token, row, col);

        return new PurchaseResponse(token, row, col, price);
    }

}
