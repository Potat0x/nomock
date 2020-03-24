package pl.potat0x.nomock.examples.bookapp;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("production")
public interface JpaBookRepository extends JpaRepository<BookEntity, Long> {
}