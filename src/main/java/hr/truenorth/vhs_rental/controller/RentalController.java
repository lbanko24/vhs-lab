package hr.truenorth.vhs_rental.controller;

import hr.truenorth.vhs_rental.exception.ResourceNotFoundException;
import hr.truenorth.vhs_rental.model.dto.RentalDto;
import hr.truenorth.vhs_rental.model.User;
import hr.truenorth.vhs_rental.model.VHS;
import hr.truenorth.vhs_rental.model.dto.VHSReturnDto;
import hr.truenorth.vhs_rental.service.RentalService;
import hr.truenorth.vhs_rental.model.Rental;
import hr.truenorth.vhs_rental.service.UserService;
import hr.truenorth.vhs_rental.service.VHSService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/rental")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;
    private final UserService userService;
    private final VHSService vhsService;

    @GetMapping
    public List<Rental> getAllRentals() {
        return rentalService.getAll();
    }

    @GetMapping("/{id}")
    public Rental getRental(@PathVariable Long id) {
        Optional<Rental> rental = rentalService.getById(id);

        if (rental.isPresent()) {
            return rental.get();
        } else {
            throw new ResourceNotFoundException("Rental not found");
        }
    }

    @PostMapping
    public Rental createRental(@RequestBody @Valid RentalDto rentalDto) {
        User user = userService.getById(rentalDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        VHS vhs = vhsService.getById(rentalDto.getVhsId())
                .orElseThrow(() -> new ResourceNotFoundException("VHS not found"));

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setVhs(vhs);
        rental.setDateRented(rentalDto.getDateRented());
        rental.setDateDue(rentalDto.getDateDue());

        return rentalService.save(rental);
    }

    @PutMapping("/{id}")
    public Rental updateRental(@PathVariable Long id, @RequestBody @Valid RentalDto rentalDto) {
        Optional<Rental> oldRental = rentalService.getById(id);
        User user = userService.getById(rentalDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        VHS vhs = vhsService.getById(rentalDto.getVhsId())
                .orElseThrow(()-> new ResourceNotFoundException("VHS not found"));

        if (oldRental.isPresent()) {
            Rental newRental = new Rental();
            newRental.setId(id);
            newRental.setVhs(vhs);
            newRental.setUser(user);
            newRental.setDateRented(rentalDto.getDateRented());
            newRental.setDateDue(rentalDto.getDateDue());

            return rentalService.save(newRental);
        } else {
            throw new ResourceNotFoundException("Rental not found");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteRental(@PathVariable Long id) {
        Optional<Rental> rental = rentalService.getById(id);

        if (rental.isPresent()) {
            rentalService.delete(id);
        } else {
            throw new ResourceNotFoundException("Rental not found");
        }
    }

    @PutMapping("/{id}/return")
    public VHSReturnDto returnVhs(@PathVariable Long id) {
        Optional<Rental> rental = rentalService.getById(id);

        if (rental.isPresent()) {
            rentalService.returnVHS(rental.get());
            float fee = rentalService.lateFee(rental.get());
            return new VHSReturnDto(rental.get().getDateReturned(), fee);
        } else {
            throw new ResourceNotFoundException("Rental not found");
        }
    }
}
