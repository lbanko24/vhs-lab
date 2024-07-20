package hr.truenorth.vhs_rental.repository;

import hr.truenorth.vhs_rental.model.Rental;
import hr.truenorth.vhs_rental.model.VHS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

/**
 * Repository used for accessing the rentals table
 */
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    /**
     * Finds rentals with conflicting date ranges (rented and due) for a given VHS and date range.
     *
     * @param vhs the vhs
     * @param start the start of the range
     * @param end the end of the date range
     * @return list of rentals with dates conflicting the given date range
     */
    @Query(value = "SELECT r FROM Rental r WHERE " +
            "r.vhs = :vhs AND " +
            ":start <= r.dateDue AND " +
            ":end >= r.dateRented AND " +
            "r.dateReturned IS NULL")
    List<Rental> findConflicts(@Param("vhs") VHS vhs, @Param("start") Date start, @Param("end") Date end);
}
