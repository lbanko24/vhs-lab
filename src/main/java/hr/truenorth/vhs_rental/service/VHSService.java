package hr.truenorth.vhs_rental.service;

import hr.truenorth.vhs_rental.model.VHS;
import hr.truenorth.vhs_rental.repository.VHSRepository;
import org.springframework.stereotype.Service;

/**
 * Service used to handle logic and access to VHS repository
 */
@Service
public class VHSService extends AbstractCrudService<VHS, VHSRepository> {

    public VHSService(VHSRepository repository) {
        super(repository);
    }

}
