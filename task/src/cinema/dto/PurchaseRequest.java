package cinema.dto;

import cinema.util.SeatCoordinateConverter;
import cinema.util.SeatCoordinates;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PurchaseRequest(int row, int column) {

    @JsonCreator
    public PurchaseRequest(@JsonProperty("row") int row, @JsonProperty("column") int column) {
        // Convert from client-facing 1-based index to internal 0-based index
        final SeatCoordinates internalCoordinates = SeatCoordinateConverter.toInternalCoordinates(row, column);
        this.row = internalCoordinates.row();
        this.column = internalCoordinates.col();
    }

}
