package hr.truenorth.vhs_rental.service;

import hr.truenorth.vhs_rental.exception.UsernameTakenException;
import hr.truenorth.vhs_rental.model.User;
import hr.truenorth.vhs_rental.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractCrudService<User, UserRepository> {

    public UserService(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public User save(User user) {
        if (repository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameTakenException("Username " + user.getUsername() + " is already taken");
        }

        return repository.save(user);
    }

}
