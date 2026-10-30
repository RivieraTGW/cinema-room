package cinema.model;

import cinema.exception.StatisticsInconsistencyException;

public class Statistics {

    private final int maxSeats;
    private int availableSeats;
    private int income; // in real life this should be double
    private int purchasedTickets;

    public Statistics(int maxSeats) {
        this.maxSeats = maxSeats;
        this.availableSeats = maxSeats;
        this.income = 0;
        this.purchasedTickets = 0;
    }

    public void incrementAvailableSeats() {
        if (availableSeats >= maxSeats) {
            throw new StatisticsInconsistencyException(
                    "Cannot increment the number of available seats above the total number of seats!");
        }
        availableSeats++;
    }

    public void decrementAvailableSeats() {
        if (availableSeats <= 0) {
            throw new StatisticsInconsistencyException(
                    "Cannot decrement the number of available seats below 0!");
        }
        availableSeats--;
    }

    public void addIncome(int amount) {
        income += amount;
    }

    public void subtractIncome(int amount) {
        income -= amount;
    }

    public void incrementPurchasedTickets() {
        if (purchasedTickets >= maxSeats) {
            throw new StatisticsInconsistencyException(
                    "Cannot increment the number of purchased tickets above the total number of seats!");
        }
        purchasedTickets++;
    }

    public void decrementPurchasedTickets() {
        if (purchasedTickets <= 0) {
            throw new StatisticsInconsistencyException("Cannot decrement the number of purchased tickets below 0!");
        }
        purchasedTickets--;
    }

    public int getIncome() {
        return income;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public int getPurchasedTickets() {
        return purchasedTickets;
    }
}
