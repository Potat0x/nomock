package pl.potat0x.nomock.examples.bookapp;

import pl.potat0x.nomock.inmemoryrepository.repository.InMemoryCrudRepository;

import static pl.potat0x.nomock.inmemoryrepository.repository.IdGenerators.IncrementalLongIdGenerator;

public class InMemoryCrudBookRepository extends InMemoryCrudRepository<BookEntity, Long> implements CrudBookRepository {
    public InMemoryCrudBookRepository() {
        super(1L, IncrementalLongIdGenerator); //use one of predefined generators
//        super(1L, id -> id + 1); //or define your own generator
    }
}
