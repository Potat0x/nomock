package pl.potat0x.nomock.examples.bookapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    private BookRepository bookRepository = new InMemoryBookRepository();

    @Test
    public void name() {
        System.out.println(bookRepository.save(new BookEntity(123L, "test")));
        System.out.println(bookRepository.findById(123L));
    }
}
