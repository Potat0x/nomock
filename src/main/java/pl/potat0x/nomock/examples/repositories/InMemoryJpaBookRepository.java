package pl.potat0x.nomock.examples.repositories;

import pl.potat0x.nomock.examples.bookapp.BookEntity;
import pl.potat0x.nomock.inmemoryrepository.repository.InMemoryJpaRepository;

import static pl.potat0x.nomock.inmemoryrepository.repository.IdGenerators.IncrementalLongIdGenerator;

public class InMemoryJpaBookRepository extends InMemoryJpaRepository<BookEntity, Long> implements JpaBookRepository {
    public InMemoryJpaBookRepository() {
        super(1L, IncrementalLongIdGenerator);
    }
}
