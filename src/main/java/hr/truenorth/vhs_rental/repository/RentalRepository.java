package hr.truenorth.vhs_rental.repository;

import hr.truenorth.vhs_rental.model.Rental;
import hr.truenorth.vhs_rental.model.VHS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query(value = "SELECT r FROM Rental r WHERE " +
            "r.vhs = :vhs AND " +
            ":start <= r.dateDue AND " +
            ":end >= r.dateRented AND " +
            "r.dateReturned IS NULL")
    List<Rental> findConflicts(@Param("vhs") VHS vhs, @Param("start") Date start, @Param("end") Date end);
}
