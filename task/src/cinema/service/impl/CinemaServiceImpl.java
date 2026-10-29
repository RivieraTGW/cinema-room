package cinema.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cinema.dto.PurchaseResponse;
import cinema.dto.SeatDTO;
import cinema.dto.SeatsResponse;
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
        // to prevent data being written to only one table
        final int price = cinemaRepository.getTicketPrice(row);
        cinemaRepository.setSeatAvailable(row, col, false);
        final UUID token = cinemaRepository.savePurchase(row, col);

        final SeatCoordinates clientCoordinates =  SeatCoordinateConverter.toClientCoordinates(row, col);
        logger.info("A new purchase has been made. Token: {}, row: {}, column: {}",
                token, clientCoordinates.row(), clientCoordinates.col());

        return new PurchaseResponse(token, row, col, price);
    }

}
