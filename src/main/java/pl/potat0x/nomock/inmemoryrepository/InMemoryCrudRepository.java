package pl.potat0x.nomock.inmemoryrepository;

import org.springframework.data.repository.CrudRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class InMemoryCrudRepository<T, ID> implements CrudRepository<T, ID> {

    private final Map<ID, T> repository = new HashMap<>();
    private final EntityRipper<T, ID> entityRipper;
    private final IdGenerator<ID> idGenerator;

    public InMemoryCrudRepository(Supplier<ID> idSupplier) {
        this.entityRipper = new EntityRipper<>(idSupplier.get().getClass());
        this.idGenerator = new IdGenerator<>(idSupplier);
    }

    public InMemoryCrudRepository(ID initialId, UnaryOperator<ID> idSuccessorFunction) {
        this.entityRipper = new EntityRipper<>(initialId.getClass());
        this.idGenerator = new IdGenerator<>(initialId, idSuccessorFunction);
    }

    @Override
    public <S extends T> S save(S entity) {
        entityRipper.getEntityId(entity)
                .ifPresentOrElse(
                        currentId -> repository.put(currentId, entity),
                        () -> repository.put(assignIdToEntity(entity), entity)
                );
        return entity;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(repository.get(id));
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

    private ID assignIdToEntity(T entity) {
        ID id = idGenerator.nextId();
        entityRipper.setEntityId(entity, id);
        return id;
    }
}
