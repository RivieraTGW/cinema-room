package cinema.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PurchaseRequest(int row, int column) {

    @JsonCreator
    public PurchaseRequest(@JsonProperty("row") int row, @JsonProperty("column") int column) {
        // Convert from client-facing 1-based index to internal 0-based index
        this.row = row - 1;
        this.column = column - 1;
    }

}
