package hr.truenorth.vhs_rental.controller;

import hr.truenorth.vhs_rental.exception.ResourceNotFoundException;
import hr.truenorth.vhs_rental.model.VHS;
import hr.truenorth.vhs_rental.service.VHSService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vhs")
@RequiredArgsConstructor
public class VHSController {

    private final VHSService vhsService;

    @GetMapping
    public List<VHS> getAllVHS() {
        return vhsService.getAll();
    }

    @GetMapping("/{id}")
    public VHS getVHS(@PathVariable Long id) {
        Optional<VHS> VHS = vhsService.getById(id);

        if (VHS.isPresent()) {
            return VHS.get();
        } else {
            throw new ResourceNotFoundException("VHS not found");
        }
    }

    @PostMapping
    public VHS createVHS(@RequestBody VHS vhs) {
        return vhsService.save(vhs);
    }

    @PutMapping("/{id}")
    public VHS updateVHS(@PathVariable Long id, @RequestBody VHS vhs) {
        Optional<VHS> oldVHS = vhsService.getById(id);

        if (oldVHS.isPresent()) {
            VHS newVHS = new VHS();
            newVHS.setId(id);
            newVHS.setTitle(vhs.getTitle());
            return vhsService.save(newVHS);
        } else {
            throw new ResourceNotFoundException("VHS not found");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteVHS(@PathVariable Long id) {
        Optional<VHS> VHS = vhsService.getById(id);

        if (VHS.isPresent()) {
            vhsService.delete(id);
        } else {
            throw  new ResourceNotFoundException("Rental not found");
        }
    }
}
