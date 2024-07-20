package hr.truenorth.vhs_rental.validation;

import hr.truenorth.vhs_rental.model.dto.RentalDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RentalDateValidator implements ConstraintValidator<DateDueAfterDateRented, RentalDto> {

    @Override
    public void initialize(DateDueAfterDateRented constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(RentalDto rental, ConstraintValidatorContext context) {
        return rental.getDateDue().after(rental.getDateRented());
    }
}
