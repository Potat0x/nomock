package pl.potat0x.nomock.examples.repositories;

import pl.potat0x.nomock.inmemoryrepository.InMemoryCrudRepository;

import java.util.UUID;

final class InMemoryUuidExampleEntityRepository extends InMemoryCrudRepository<UuidExampleEntity, UUID> implements UuidExampleEntityRepository {
    public InMemoryUuidExampleEntityRepository() {
        super(UUID::randomUUID);
    }
}
