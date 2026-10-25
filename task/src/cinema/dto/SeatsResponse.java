package cinema.dto;

import java.util.List;

public record SeatsResponse(int rows, int columns, List<SeatDTO> seats) { }
