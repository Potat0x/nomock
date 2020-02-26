package pl.potat0x.nomock.examples.bookapp;

import pl.potat0x.nomock.inmemoryrepository.InMemoryCrudRepository;

public class InMemoryBookRepository extends InMemoryCrudRepository<BookEntity, Long> implements BookRepository {
}
