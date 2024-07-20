package hr.truenorth.vhs_rental.controller;

import hr.truenorth.vhs_rental.exception.ResourceNotFoundException;
import hr.truenorth.vhs_rental.model.User;
import hr.truenorth.vhs_rental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        Optional<User> VHS = userService.getById(id);

        if (VHS.isPresent()) {
            return VHS.get();
        } else {
            throw new ResourceNotFoundException("VHS not found");
        }
    }

    @PostMapping
    public User createUser(@RequestBody User vhs) {
        return userService.save(vhs);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> oldVHS = userService.getById(id);

        if (oldVHS.isPresent()) {
            User newVHS = new User();
            newVHS.setId(id);
            newVHS.setUsername(user.getUsername());
            return userService.save(newVHS);
        } else {
            throw new ResourceNotFoundException("VHS not found");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteVHS(@PathVariable Long id) {
        Optional<User> VHS = userService.getById(id);

        if (VHS.isPresent()) {
            userService.delete(id);
        } else {
            throw  new ResourceNotFoundException("Rental not found");
        }
    }
}
