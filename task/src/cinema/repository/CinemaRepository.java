package cinema.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

import cinema.exception.ErrorMessages;
import cinema.exception.InvalidTokenException;
import cinema.model.Purchase;
import cinema.model.Room;
import cinema.model.Seat;
import cinema.model.Statistics;
import cinema.util.HashUtil;
import cinema.util.SeatCoordinates;
import cinema.util.StatisticsData;

/**
 * This is a simple implementation of an in-memory repository. This project will not use a database,
 * so I've taken the liberty and stored all the values in one repository. If a database were to be used,
 * this repository would have to be separated into more repositories, so that they could be mapped to
 * tables of a database.
 */
@Component
public class CinemaRepository {

    /**
     * These values are hardcoded only for the purpose of this project.
     * They are used to initialize the cinema repository.
     */
    private final int numRows = 9;
    private final int numCols = 9;
    private byte[] passwordHash = HashUtil.calculateSha256Hash("super_secret");

    /**
     * Seats on or below this row are more expensive.
     */
    private final int expensiveRowThreshold = 3;
    private final int expensiveSeatCost = 10;
    private final int cheapSeatCost = 8;

    private final Room room;
    private final ConcurrentMap<UUID, Purchase> purchases = new ConcurrentHashMap<>();
    private final Statistics statistics;

    public CinemaRepository() {
        this.room = new Room(numRows, numCols);
        this.statistics = new Statistics(numRows * numCols);
    }

    public List<SeatCoordinates> getAvailableSeats() {
        // Starting iterating from one, because seat indices are 1-based
        final List<SeatCoordinates> availableSeats = new ArrayList<>();
        for (int row = 1; row <= room.getNumRows(); row++) {
            for (int col = 1; col <= room.getNumCols(); col++) {
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

        // Update the statistics
        statistics.incrementPurchasedTickets();
        statistics.decrementAvailableSeats();
        statistics.addIncome(getTicketPrice(row));

        return token;
    }

    public SeatCoordinates deletePurchase(final UUID token) {
        if (!purchases.containsKey(token)) {
            throw new InvalidTokenException(ErrorMessages.INVALID_TOKEN_ERR);
        }

        final Purchase purchase = purchases.remove(token);

        // Update the statistics
        statistics.decrementPurchasedTickets();
        statistics.incrementAvailableSeats();
        statistics.subtractIncome(getTicketPrice(purchase.seatCoordinates().row()));

        return purchase.seatCoordinates();
    }

    public StatisticsData getStatistics() {
        return new StatisticsData(
                statistics.getIncome(),
                statistics.getAvailableSeats(),
                statistics.getPurchasedTickets());
    }

    public int getTicketPrice(final int row) {
        if (row < 1 || row > room.getNumRows()) {
            throw new IllegalArgumentException(ErrorMessages.ROW_COL_OUT_OF_BOUNDS_ERR);
        }

        return row >= expensiveRowThreshold ? cheapSeatCost : expensiveSeatCost;
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

    public byte[] getPasswordHash() {
        return passwordHash.clone();
    }
}
