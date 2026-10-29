package cinema.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import cinema.exception.ErrorMessages;
import cinema.util.SeatCoordinateConverter;
import cinema.util.SeatCoordinates;

public record PurchaseRequest(int row, int column) {

    @JsonCreator
    public PurchaseRequest(@JsonProperty("row") int row, @JsonProperty("column") int column) {
        if (row < 1 || column < 1) {
            throw new IllegalArgumentException(ErrorMessages.ROW_COL_OUT_OF_BOUNDS_ERR);
        }

        // Convert from client-facing 1-based index to internal 0-based index
        final SeatCoordinates internalCoordinates = SeatCoordinateConverter.toInternalCoordinates(row, column);
        this.row = internalCoordinates.row();
        this.column = internalCoordinates.col();
    }

}
