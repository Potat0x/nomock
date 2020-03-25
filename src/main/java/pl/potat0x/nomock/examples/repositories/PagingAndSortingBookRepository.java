package pl.potat0x.nomock.examples.repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.potat0x.nomock.examples.bookapp.BookEntity;

@Repository
@Profile("production")
public interface PagingAndSortingBookRepository extends PagingAndSortingRepository<BookEntity, Long> {
}
