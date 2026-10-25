package cinema.repository;

import org.springframework.stereotype.Component;

import cinema.config.CinemaConfiguration;
import cinema.model.Room;

/**
 * This is a simple implementation of an in-memory repository. This project will not use a database,
 * so I've taken the liberty and stored all the values in one repository. If a database were to be used,
 * this repository would have to be separated into more repositories, so that they could be mapped to
 * tables of a database.
 */
@Component
public class CinemaRepository {

    private final Room room;

    public CinemaRepository(CinemaConfiguration cinemaConfiguration) {
        this.room = new Room(cinemaConfiguration.getNumRows(), cinemaConfiguration.getNumCols());
    }

    public Room getRoom() {
        return room;
    }
}
