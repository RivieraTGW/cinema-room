package cinema.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CinemaConfiguration {

    /**
     * These values are hardcoded only for the purpose of this task. They are used to initialize
     * the cinema repository (check CinemaRepository.java for more information).
     */
    private final int numRows = 9;
    private final int numCols = 9;

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }
}
