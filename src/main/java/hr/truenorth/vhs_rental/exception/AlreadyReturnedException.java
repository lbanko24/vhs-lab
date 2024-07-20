package hr.truenorth.vhs_rental.exception;

import java.sql.Date;

/**
 * Exception thrown when attempting to return a VHS for a rental that is already returned
 */
public class AlreadyReturnedException extends RuntimeException {
    private final Date returnDate;

    public AlreadyReturnedException(String message, Date returnDate) {
        super(message);
        this.returnDate = returnDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }
}
