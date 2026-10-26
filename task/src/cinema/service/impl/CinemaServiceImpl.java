package cinema.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cinema.dto.SeatDTO;
import cinema.dto.SeatsResponse;
import cinema.repository.CinemaRepository;
import cinema.service.CinemaService;
import cinema.util.Pair;

@Service
public class CinemaServiceImpl implements CinemaService {

    private final CinemaRepository cinemaRepository;

    public CinemaServiceImpl(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public SeatsResponse getAvailableSeats() {
        final List<Pair<Integer, Integer>> availableSeats = cinemaRepository.getAvailableSeats();
        final List<SeatDTO> availableSeatsDTO = new ArrayList<>();

        for (final Pair<Integer, Integer> seat : availableSeats) {
            availableSeatsDTO.add(new SeatDTO(seat.key(), seat.value()));
        }

        return new SeatsResponse(cinemaRepository.getNumRows(), cinemaRepository.getNumCols(), availableSeatsDTO);
    }

    @Override
    public boolean purchaseTicket(Map<String, String> result, int row, int col) {
        boolean successful = false;

        try {
            if (cinemaRepository.isSeatAvailable(row, col)) {
                result.put("price", Integer.toString(cinemaRepository.getTicketPrice(row)));
                cinemaRepository.setSeatAvailable(row, col, false);
                successful = true;
            } else {
                result.put("error", "The ticket has been already purchased!");
            }
        } catch (IllegalArgumentException e) {
            result.put("error", "The number of a row or a column is out of bounds!");
        }

        return successful;
    }

}
