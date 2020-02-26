package pl.potat0x.nomock.inmemoryrepository;

import org.springframework.data.repository.CrudRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryCrudRepository<T, ID> implements CrudRepository<T, ID> {

    private final Map<ID, T> repository = new HashMap<>();
    private long nextId = 1;

    @Override
    public <S extends T> S save(S entity) {
        if (!hasId(entity)) {
            setNextId(entity);
        }

        repository.put(EntityRipper.<ID>getEntityId(entity).get(), entity);
        return entity;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(ID id) {
        return false;
    }

    @Override
    public Iterable<T> findAll() {
        return null;
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(ID id) {

    }

    @Override
    public void delete(T t) {

    }

    @Override
    public void deleteAll(Iterable<? extends T> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    private <S extends T> void setNextId(S entity) {
        EntityRipper.setEntityId(entity, nextId += 10);
    }

    private <S extends T> boolean hasId(S entity) {
        return EntityRipper.getEntityId(entity).isPresent();
    }
}
