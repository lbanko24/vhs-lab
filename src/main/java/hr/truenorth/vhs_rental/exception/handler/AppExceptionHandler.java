package hr.truenorth.vhs_rental.exception.handler;

import hr.truenorth.vhs_rental.exception.AlreadyRentedException;
import hr.truenorth.vhs_rental.exception.AlreadyReturnedException;
import hr.truenorth.vhs_rental.exception.ResourceNotFoundException;
import hr.truenorth.vhs_rental.exception.UsernameTakenException;
import hr.truenorth.vhs_rental.model.Rental;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class AppExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);

    private final MessageSource messageSource;

    public AppExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(ResourceNotFoundException ex, Locale locale) {
        logger.info(ex.getLocalizedMessage());

        return messageSource.getMessage("error.not_found", null, locale);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAlreadyRented(AlreadyRentedException ex, Locale locale) {
        logger.info(ex.getLocalizedMessage());
        StringBuilder sb = new StringBuilder(messageSource.getMessage("error.already_rented", null, locale));

        for (Rental rental : ex.getConflictingRentals()) {
            sb.append('\n');
            sb.append('\t');
            sb.append(rental);
        }

        return sb.toString();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAlreadyReturned(AlreadyReturnedException ex, Locale locale) {
        logger.info(ex.getLocalizedMessage());
        String message = messageSource.getMessage("error.already_returned", null, locale);

        return message + " " + ex.getReturnDate();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConstraintViolation(ConstraintViolationException ex) {
        StringBuilder sb = new StringBuilder();

        for (ConstraintViolation violation : ex.getConstraintViolations()) {
            sb.append(violation.getMessage());
            sb.append('\n');
        }

        logger.info(sb.toString());
        return sb.toString();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMethodArgumentNotValid(MethodArgumentNotValidException ex, Locale locale) {
        logger.info(ex.getLocalizedMessage());

        return messageSource.getMessage("error.invalid_arguments", null, locale);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleUsernameTaken(UsernameTakenException ex, Locale locale) {
        logger.info(ex.getLocalizedMessage());

        return messageSource.getMessage("error.username_taken", null, locale);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleOther(Exception ex, Locale locale) {
        logger.error(ex.getLocalizedMessage());

        String message = messageSource.getMessage("error.unexpected", null, locale);
        return message;
    }
}
