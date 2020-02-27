package pl.potat0x.nomock.examples.repositories;

import pl.potat0x.nomock.inmemoryrepository.InMemoryCrudRepository;

import java.util.UUID;

import static pl.potat0x.nomock.inmemoryrepository.IdGenerators.UuidGenerator;

final class InMemoryUuidExampleEntityRepository extends InMemoryCrudRepository<UuidExampleEntity, UUID> implements UuidExampleEntityRepository {
    public InMemoryUuidExampleEntityRepository() {
        super(UuidGenerator);
    }
}
