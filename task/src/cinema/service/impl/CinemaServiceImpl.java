package cinema.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cinema.dto.*;
import cinema.exception.ErrorMessages;
import cinema.exception.SeatAlreadyPurchasedException;
import cinema.repository.CinemaRepository;
import cinema.service.CinemaService;
import cinema.util.SeatCoordinateConverter;
import cinema.util.SeatCoordinates;

@Service
public class CinemaServiceImpl implements CinemaService {

    private static final Logger logger = LoggerFactory.getLogger(CinemaServiceImpl.class);

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
        // Note: If a real database was used, this method would be annotated as @Transactional
        // to prevent data being written to only one table.

        if (!cinemaRepository.isSeatAvailable(row, col)) {
            throw new SeatAlreadyPurchasedException(ErrorMessages.SEAT_ALREADY_PURCHASED_ERR);
        }

        // To-Do: The current solution has a weakness.
        // If the server went down between setSeatAvailable() and savePurchase(), we would
        // get into a state where the ticket would be marked as 'unavailable', but we would
        // have no record of the purchase.
        final int price = cinemaRepository.getTicketPrice(row);
        cinemaRepository.setSeatAvailable(row, col, false);
        final UUID token = cinemaRepository.savePurchase(row, col);

        final SeatCoordinates clientCoordinates =  SeatCoordinateConverter.toClientCoordinates(row, col);
        logger.info("A new purchase has been made. Token: {}, row: {}, column: {}",
                token, clientCoordinates.row(), clientCoordinates.col());

        return new PurchaseResponse(token, row, col, price);
    }

    @Override
    public synchronized ReturnResponse returnTicket(final UUID token) {
        // Note: In a real-life situation we would probably not want to delete a purchase,
        // but rather just mark it as 'refunded'. I chose to delete it for simplicity.
        // Also, this method would be annotated as @Transactional if a real database was used.

        // To-Do: The current solution has a weakness.
        // If the server went down between deletePurchase() and setSeatAvailable(), we would
        // get into a state where the purchase would be deleted but the seat would not be marked
        // as available again. This way, the seat would remain unavailable indefinitely.
        final SeatCoordinates seatCoordinates = cinemaRepository.deletePurchase(token);
        cinemaRepository.setSeatAvailable(seatCoordinates.row(), seatCoordinates.col(), true);
        final int price = cinemaRepository.getTicketPrice(seatCoordinates.row());

        final SeatCoordinates clientCoordinates = SeatCoordinateConverter.toClientCoordinates(
                seatCoordinates.row(), seatCoordinates.col());
        logger.info("A ticket has been refunded. Token: {}, row: {}, column: {}",
                token, clientCoordinates.row(), clientCoordinates.col());

        return new ReturnResponse(new TicketDTO(seatCoordinates.row(), seatCoordinates.col(), price));
    }

}
