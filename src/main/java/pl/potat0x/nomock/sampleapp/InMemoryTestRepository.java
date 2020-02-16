package pl.potat0x.nomock.sampleapp;

import pl.potat0x.nomock.inmemoryrepository.InMemoryCrudRepository;

class InMemoryTestRepository extends InMemoryCrudRepository<TestEntity, Long> implements TestRepository {
}
