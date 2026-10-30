package cinema.dto;

import cinema.exception.ErrorMessages;

public record PurchaseRequest(int row, int column) {

    public PurchaseRequest {
        if (row < 1 || column < 1) {
            throw new IllegalArgumentException(ErrorMessages.ROW_COL_OUT_OF_BOUNDS_ERR);
        }
    }
}
