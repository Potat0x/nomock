package pl.potat0x.nomock.inmemoryrepository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.repository.CrudRepository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class InMemoryCrudRepository<T, ID> implements CrudRepository<T, ID> {

    private final Map<ID, T> repository = new LinkedHashMap<>();
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
        assertEntityNotNull(entity);
        entityRipper.getEntityId(entity)
                .ifPresentOrElse(
                        currentId -> repository.put(currentId, entity),
                        () -> repository.put(assignIdToEntity(entity), entity)
                );
        return entity;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        assertEntitiesNotNull(entities);
        return iterableToStream(entities)
                .map(this::save)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<T> findById(ID id) {
        assertIdNotNull(id);
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public boolean existsById(ID id) {
        assertIdNotNull(id);
        return repository.containsKey(id);
    }

    @Override
    public Iterable<T> findAll() {
        return repository.values();
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        assertIdsNotNull(ids);
        return iterableToStream(ids)
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return repository.size();
    }

    @Override
    public void deleteById(ID id) {
        assertIdNotNull(id);
        if (repository.containsKey(id)) {
            repository.remove(id);
        } else {
            throw new EmptyResultDataAccessException(String.format("no entity with id %s exists!", id), 1);
        }
    }

    @Override
    public void delete(T entity) {
        assertEntityNotNull(entity);
        entityRipper.getEntityId(entity)
                .ifPresent(repository::remove);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        assertEntitiesNotNull(entities);
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        repository.clear();
    }

    private ID assignIdToEntity(T entity) {
        ID id = idGenerator.nextId();
        entityRipper.setEntityId(entity, id);
        return id;
    }

    private void assertIdNotNull(ID id) {
        assertNotNull(id, "id must not be null");
    }

    private void assertIdsNotNull(Iterable<ID> ids) {
        if (ids == null) {
            throw new IllegalArgumentException("ids must not be null");
        }
        ids.forEach(this::assertIdNotNull);
    }

    private <S extends T> void assertEntityNotNull(S entity) {
        assertNotNull(entity, "entity must not be null");
    }

    private <S extends T> void assertEntitiesNotNull(Iterable<S> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("entities must not be null");
        }
        entities.forEach(this::assertEntityNotNull);
    }

    private void assertNotNull(Object object, String exceptionMessage) {
        if (object == null) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    private static <T> Stream<T> iterableToStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
