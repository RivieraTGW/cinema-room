package cinema.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import cinema.dto.SeatDTO;
import cinema.dto.SeatsResponse;
import cinema.model.Room;
import cinema.model.Seat;
import cinema.repository.CinemaRepository;
import cinema.service.CinemaService;

@Service
public class CinemaServiceImpl implements CinemaService {

    private final CinemaRepository cinemaRepository;

    public CinemaServiceImpl(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public SeatsResponse getAvailableSeats() {
        final Room room = cinemaRepository.getRoom();
        final List<SeatDTO> availableSeats = new ArrayList<>();

        for (int row = 0; row < room.getNumRows(); row++) {
            for (int col = 0; col < room.getNumCols(); col++) {
                final Seat seat = room.getSeat(row, col);
                if (seat.isAvailable()) {
                    availableSeats.add(new SeatDTO(seat.getRow(), seat.getCol()));
                }
            }
        }

        return new SeatsResponse(room.getNumRows(), room.getNumCols(), availableSeats);
    }

}
