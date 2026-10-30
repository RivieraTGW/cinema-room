package cinema.service.impl;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cinema.dto.*;
import cinema.exception.ErrorMessages;
import cinema.exception.IncorrectPasswordException;
import cinema.exception.SeatAlreadyPurchasedException;
import cinema.repository.CinemaRepository;
import cinema.service.CinemaService;
import cinema.util.HashUtil;
import cinema.util.SeatCoordinates;
import cinema.util.StatisticsData;

@Service
public class CinemaServiceImpl implements CinemaService {

    private static final Logger logger = LoggerFactory.getLogger(CinemaServiceImpl.class);

    private final CinemaRepository cinemaRepository;

    public CinemaServiceImpl(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public synchronized SeatsResponse getAvailableSeats() {
        // Note: If a real database was used, this would've been a textbook demonstration of an N+1
        // problem. We're getting the list of available seats first and then getting their
        // prices. The solution for this is to make one query using join.
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
    public synchronized PurchaseResponse purchaseTicket(final PurchaseRequest purchaseRequest) {
        // Note: If a real database was used, this method would be annotated as @Transactional
        // to prevent data being written to only one table.

        // To-Do: The current solution has a weakness.
        // If the server went down between setSeatAvailable() and savePurchase(), we would
        // get into a state where the ticket would be marked as 'unavailable', but we would
        // have no record of the purchase.

        final int row = purchaseRequest.row();
        final int col = purchaseRequest.column();

        if (!cinemaRepository.isSeatAvailable(row, col)) {
            throw new SeatAlreadyPurchasedException(ErrorMessages.SEAT_ALREADY_PURCHASED_ERR);
        }

        final int price = cinemaRepository.getTicketPrice(row);
        cinemaRepository.setSeatAvailable(row, col, false);
        final UUID token = cinemaRepository.savePurchase(row, col);

        logger.info("A new purchase has been made. Token: {}, row: {}, column: {}", token, row, col);

        return new PurchaseResponse(token, row, col, price);
    }

    @Override
    public synchronized TicketDTO returnTicket(final ReturnRequest returnRequest) {
        // Note: In a real-life situation we would probably not want to delete a purchase,
        // but rather just mark it as 'refunded'. I chose to delete it for simplicity.
        // Also, this method would be annotated as @Transactional if a real database was used
        // to make sure that this method would pass as a whole or fail.

        // To-Do: The current solution has a weakness.
        // If the server went down between deletePurchase() and setSeatAvailable(), we would
        // get into a state where the purchase would be deleted but the seat would not be marked
        // as available again. This way, the seat would remain unavailable indefinitely.

        final SeatCoordinates seatCoordinates = cinemaRepository.deletePurchase(returnRequest.token());
        cinemaRepository.setSeatAvailable(seatCoordinates.row(), seatCoordinates.col(), true);
        final int price = cinemaRepository.getTicketPrice(seatCoordinates.row());

        logger.info("A ticket has been refunded. Token: {}, row: {}, column: {}",
                returnRequest.token(), seatCoordinates.row(), seatCoordinates.col());

        return new TicketDTO(seatCoordinates.row(), seatCoordinates.col(), price);
    }

    @Override
    public synchronized StatsResponse getStatistics(final String password) {
        if (password == null) {
            throw new IncorrectPasswordException(ErrorMessages.INCORRECT_PASSWORD_ERR);
        }

        // Incorrect password
        final byte[] passwordHash = HashUtil.calculateSha256Hash(password);
        if (!MessageDigest.isEqual(passwordHash, cinemaRepository.getPasswordHash())) {
            throw new IncorrectPasswordException(ErrorMessages.INCORRECT_PASSWORD_ERR);
        }

        final StatisticsData statistics = cinemaRepository.getStatistics();

        return new StatsResponse(statistics.income(), statistics.availableSeats(), statistics.purchasedTickets());
    }
}
