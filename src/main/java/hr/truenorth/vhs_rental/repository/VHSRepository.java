package hr.truenorth.vhs_rental.repository;

import hr.truenorth.vhs_rental.model.VHS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VHSRepository extends JpaRepository<VHS, Long> {
}
