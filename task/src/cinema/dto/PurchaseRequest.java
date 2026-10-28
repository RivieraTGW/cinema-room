package cinema.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import cinema.util.SeatCoordinateConverter;
import cinema.util.SeatCoordinates;

public record PurchaseRequest(int row, int column) {

    @JsonCreator
    public PurchaseRequest(@JsonProperty("row") int row, @JsonProperty("column") int column) {
        // Convert from client-facing 1-based index to internal 0-based index
        final SeatCoordinates internalCoordinates = SeatCoordinateConverter.toInternalCoordinates(row, column);
        this.row = internalCoordinates.row();
        this.column = internalCoordinates.col();
    }

}
