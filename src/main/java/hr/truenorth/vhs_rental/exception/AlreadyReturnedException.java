package hr.truenorth.vhs_rental.exception;

import java.sql.Date;

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
