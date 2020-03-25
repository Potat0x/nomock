package pl.potat0x.nomock.examples.bookapp;

import pl.potat0x.nomock.inmemoryrepository.repository.IdGenerators;
import pl.potat0x.nomock.inmemoryrepository.repository.InMemoryJpaRepository;

class InMemoryBookRepository extends InMemoryJpaRepository<BookEntity, Long> implements BookRepository {
    public InMemoryBookRepository() {
        super(1L, IdGenerators.IncrementalLongIdGenerator);
    }
}
