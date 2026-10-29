package cinema.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

import cinema.exception.ErrorMessages;
import cinema.exception.SeatAlreadyPurchasedException;
import cinema.model.Purchase;
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
    private final ConcurrentMap<UUID, Purchase> purchases = new ConcurrentHashMap<>();

    public CinemaRepository() {
        this.room = new Room(numRows, numCols);
    }

    public List<SeatCoordinates> getAvailableSeats() {
        final List<SeatCoordinates> availableSeats = new ArrayList<>();

        for (int row = 0; row < room.getNumRows(); row++) {
            for (int col = 0; col < room.getNumCols(); col++) {
                final Seat seat = room.getSeat(row, col);
                if (seat.isAvailable()) {
                    availableSeats.add(new SeatCoordinates(row, col));
                }
            }
        }

        return availableSeats;
    }

    public UUID savePurchase(final int row, final int col) {
        // This is an edge case. In the off chance that the same UUID
        // is generated for two purchases, regenerate a new UUID.
        UUID token;
        do {
            token = UUID.randomUUID();
        } while (purchases.containsKey(token));

        final Purchase purchase = new Purchase(new SeatCoordinates(row, col));
        purchases.put(token, purchase);

        return token;
    }

    public boolean isSeatAvailable(final int row, final int col) {
        return room.getSeat(row, col).isAvailable();
    }

    public void setSeatAvailable(final int row, final int col, final boolean available) {
        if (!isSeatAvailable(row, col)) {
            throw new SeatAlreadyPurchasedException(ErrorMessages.SEAT_ALREADY_PURCHASED_ERR);
        }

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
        if (row < 0 || row >= room.getNumRows()) {
            throw new IllegalArgumentException(ErrorMessages.ROW_COL_OUT_OF_BOUNDS_ERR);
        }

        return row > expensiveRowThreshold ? cheapSeatCost : expensiveSeatCost;
    }

}
