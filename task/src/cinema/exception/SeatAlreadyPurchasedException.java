package cinema.exception;

public class SeatAlreadyPurchasedException extends RuntimeException {

    public SeatAlreadyPurchasedException(String message) {
        super(message);
    }
}
