package cinema.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CinemaConfiguration {

    public int getNumRows() {
        return 9;
    }

    public int getNumCols() {
        return 9;
    }
}
