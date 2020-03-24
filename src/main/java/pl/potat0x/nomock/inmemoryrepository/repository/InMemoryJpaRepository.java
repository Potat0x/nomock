package pl.potat0x.nomock.inmemoryrepository.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.potat0x.nomock.inmemoryrepository.InMemoryRepositoryException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class InMemoryJpaRepository<T, ID> extends InMemoryPagingAndSortingRepository<T, ID> implements JpaRepository<T, ID> {

    public InMemoryJpaRepository(Supplier<ID> idSupplier) {
        super(idSupplier);
    }

    public InMemoryJpaRepository(ID initialId, UnaryOperator<ID> idSuccessorFunction) {
        super(initialId, idSuccessorFunction);
    }

    @Override
    public List<T> findAll() {
        return iterableToList(super.findAll());
    }

    @Override
    public List<T> findAll(Sort sort) {
        return iterableToList(super.findAll(sort));
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        return iterableToList(super.findAllById(ids));
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        return iterableToList(super.saveAll(entities));
    }

    @Override
    public void flush() {
        //nothing to flush
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        return save(entity);
    }

    @Override
    public void deleteInBatch(Iterable<T> entities) {
        deleteAll(entities);
    }

    @Override
    public void deleteAllInBatch() {
        deleteAll();
    }

    @Override
    public T getOne(ID id) {
        return findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("entity with id=" + id + " not found");
        });
    }

    @Override
    public <S extends T> Optional<S> findOne(Example<S> example) {
        throw examplesAreNotSupported();
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        throw examplesAreNotSupported();
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        throw examplesAreNotSupported();
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw examplesAreNotSupported();
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        throw examplesAreNotSupported();
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        throw examplesAreNotSupported();
    }

    private InMemoryRepositoryException examplesAreNotSupported() {
        return new InMemoryRepositoryException("Query by Example API is not supported");
    }
}
