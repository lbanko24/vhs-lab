package hr.truenorth.vhs_rental.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RentalDateValidator.class)
public @interface DateDueAfterDateRented {
    String message() default "Due date must be after borrowing date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
