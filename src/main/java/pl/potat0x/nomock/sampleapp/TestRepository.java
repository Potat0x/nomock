package pl.potat0x.nomock.sampleapp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface TestRepository extends CrudRepository<TestEntity, Long> {
}
