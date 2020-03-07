package pl.potat0x.nomock.examples.bookapp;

import pl.potat0x.nomock.inmemoryrepository.repository.InMemoryPagingAndSortingRepository;

import static pl.potat0x.nomock.inmemoryrepository.repository.IdGenerators.IncrementalLongIdGenerator;

public class InMemoryPagingAndSortingBookRepository extends InMemoryPagingAndSortingRepository<BookEntity, Long> implements PagingAndSortingBookRepository {
    public InMemoryPagingAndSortingBookRepository() {
        super(1L, IncrementalLongIdGenerator);
    }
}
