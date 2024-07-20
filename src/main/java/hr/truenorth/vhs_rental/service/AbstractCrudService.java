package hr.truenorth.vhs_rental.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service used to handle and access to a given repository.
 *
 * @param <T> the entity
 * @param <R> the repository
 */
public abstract class AbstractCrudService<T, R extends JpaRepository<T, Long>> {

    protected final R repository;

    protected AbstractCrudService(R repository) {
        this.repository = repository;
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    public Optional<T> getById(Long id) {
        return repository.findById(id);
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
