package cinema.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import cinema.model.Room;
import cinema.model.Seat;
import cinema.util.SeatCoordinates;

/**
 * This is a simple implementation of an in-memory repository. This project will not use a database,
 * so I've taken the liberty and stored all the values in one repository. If a database were to be used,
 * this repository would have to be separated into more repositories, so that they could be mapped to
 * tables of a database.
 */
@Component
public class CinemaRepository {

    /**
     * These values are hardcoded only for the purpose of this task.
     * They are used to initialize the cinema repository.
     */
    private final int numRows = 9;
    private final int numCols = 9;

    /**
     * Seats on or below this row are more expensive.
     */
    private final int expensiveRowThreshold = 3;
    private final int expensiveSeatCost = 10;
    private final int cheapSeatCost = 8;

    private final Room room;

    public CinemaRepository() {
        this.room = new Room(numRows, numCols);
    }

    public List<SeatCoordinates<Integer, Integer>> getAvailableSeats() {
        final List<SeatCoordinates<Integer, Integer>> availableSeats = new ArrayList<>();

        for (int row = 0; row < room.getNumRows(); row++) {
            for (int col = 0; col < room.getNumCols(); col++) {
                final Seat seat = room.getSeat(row, col);
                if (seat.isAvailable()) {
                    availableSeats.add(new SeatCoordinates<>(row, col));
                }
            }
        }

        return availableSeats;
    }

    public boolean isSeatAvailable(final int row, final int col) {
        return room.getSeat(row, col).isAvailable();
    }

    public void setSeatAvailable(final int row, final int col, final boolean available) {
        room.getSeat(row, col).setAvailable(available);
    }

    public int getNumRows() {
        return room.getNumRows();
    }

    public int getNumCols() {
        return room.getNumCols();
    }

    /**
     * Returns the price of a ticket at the specified row.
     * Assumes the row value is already validated by the caller.
     * Rows above the threshold are cheaper; rows at or below the threshold are more expensive.
     */
    public int getTicketPrice(final int row) {
        return row > expensiveRowThreshold ? cheapSeatCost : expensiveSeatCost;
    }

}
