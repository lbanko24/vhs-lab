package hr.truenorth.vhs_rental.exception;

import hr.truenorth.vhs_rental.model.Rental;

import java.util.List;

/**
 * Exception thrown when attempting to rent during a date range already rented
 */
public class AlreadyRentedException extends RuntimeException {

    private final List<Rental> conflictingRentals;

    public AlreadyRentedException(String message, List<Rental> conflictingRentals) {
        super(message);
        this.conflictingRentals = conflictingRentals;
    }

    public List<Rental> getConflictingRentals() {
        return conflictingRentals;
    }
}
