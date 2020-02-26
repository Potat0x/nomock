package pl.potat0x.nomock.examples.bookapp;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("production")
public interface BookRepository extends CrudRepository<BookEntity, Long> {
}
