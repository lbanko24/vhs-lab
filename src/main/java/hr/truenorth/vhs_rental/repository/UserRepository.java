package hr.truenorth.vhs_rental.repository;

import hr.truenorth.vhs_rental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository used for accessing the users table.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds user with the given username.
     *
     * @param username the username
     * @return optional containing the user, or an empty optional if not found
     */
    Optional<User> findByUsername(String username);
}
