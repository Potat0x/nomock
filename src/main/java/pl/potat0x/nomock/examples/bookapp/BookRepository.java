package pl.potat0x.nomock.examples.bookapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BookRepository extends JpaRepository<BookEntity, Long> {
}
