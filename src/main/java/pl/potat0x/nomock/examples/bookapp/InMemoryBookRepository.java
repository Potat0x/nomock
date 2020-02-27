package pl.potat0x.nomock.examples.bookapp;

import pl.potat0x.nomock.inmemoryrepository.InMemoryCrudRepository;

import static pl.potat0x.nomock.inmemoryrepository.IdGenerators.IncrementalLongIdGenerator;

public final class InMemoryBookRepository extends InMemoryCrudRepository<BookEntity, Long> implements BookRepository {
    public InMemoryBookRepository() {
        super(0L, IncrementalLongIdGenerator); //use one of predefined generators
        //super(0L, id -> id + 1); //or define your own generator
    }
}
