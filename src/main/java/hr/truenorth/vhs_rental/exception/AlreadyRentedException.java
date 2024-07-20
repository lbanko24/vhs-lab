package hr.truenorth.vhs_rental.exception;

import hr.truenorth.vhs_rental.model.Rental;

import java.util.List;

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
