package hr.truenorth.vhs_rental.service;

import hr.truenorth.vhs_rental.exception.AlreadyRentedException;
import hr.truenorth.vhs_rental.exception.AlreadyReturnedException;
import hr.truenorth.vhs_rental.model.Rental;
import hr.truenorth.vhs_rental.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@PropertySource("classpath:fee.yml")
public class RentalService extends AbstractCrudService<Rental, RentalRepository> {

    @Value("${daily_fee}")
    private float dailyFee;

    public RentalService(RentalRepository repository) {
        super(repository);
    }

    @Override
    public Rental save(Rental rental) {
        // Make sure that the same vhs can't have multiple rentals on the same date
        List<Rental> conflicts = repository.findConflicts(rental.getVhs(), rental.getDateRented(), rental.getDateDue());
        if (!conflicts.isEmpty()) {
            throw new AlreadyRentedException("This VHS was already reserved", conflicts);
        }

        return repository.save(rental);
    }

    public float lateFee(Rental rental) {
        if (rental.getDateReturned() == null || Date.valueOf(LocalDate.now()).before(rental.getDateDue())) {
            return 0;
        }

        long days = rental.getDateDue().toLocalDate()
                .datesUntil(rental.getDateReturned().toLocalDate())
                .count();

        return days * dailyFee;
    }

    public void returnVHS(Rental rental) {
        if (rental.getDateReturned() != null) {
            throw new AlreadyReturnedException("VHS already returned", rental.getDateReturned());
        }

        rental.setDateReturned(Date.valueOf(LocalDate.now()));
        repository.save(rental);
    }
}
