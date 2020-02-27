package pl.potat0x.nomock.examples.bookapp;

import pl.potat0x.nomock.inmemoryrepository.InMemoryCrudRepository;

public final class InMemoryBookRepository extends InMemoryCrudRepository<BookEntity, Long> implements BookRepository {
    public InMemoryBookRepository() {
        super(0L, id -> id + 1);
    }
}
