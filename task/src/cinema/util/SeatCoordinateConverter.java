package cinema.util;

public final class SeatCoordinateConverter {

    private SeatCoordinateConverter() { }

    /**
     * Converts from internal 0-based coordinates to client-facing 1-based coordinates.
     */
    public static SeatCoordinates toClientCoordinates(final int row, final int col) {
        return new SeatCoordinates(row + 1, col + 1);
    }

    /**
     * Converts from client-facing 1-based coordinates to internal 0-based coordinates.
     */
    public static SeatCoordinates toInternalCoordinates(final int row, final int col) {
        return new SeatCoordinates(row - 1, col - 1);
    }
}
