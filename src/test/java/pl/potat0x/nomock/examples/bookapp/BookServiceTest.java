package pl.potat0x.nomock.examples.bookapp;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.List;

class BookServiceTest {

    BookService bookService = new BookService(new InMemoryBookRepository());

    @Test
    void inMemoryRepositoryDemo() {
        List.of(new BookDto("The Godfather"),
                new BookDto("1984"),
                new BookDto("Clean Code")
        ).forEach(bookService::createBook);

        prettyPrint("All books", bookService.getAllBooks());
        prettyPrint("All books sorted by name (asc)", bookService.getAllBooks(Sort.by("name").ascending()));
        prettyPrint("All books sorted by name (desc)", bookService.getAllBooks(Sort.by("name").descending()));

        prettyPrint("Get book by id=2", bookService.getBookById(2L));
        bookService.deleteBook(2L);
        prettyPrint("All books, after deleting book with id=2", bookService.getAllBooks());
        prettyPrint("Get book by id=2", bookService.getBookById(2L));

        bookService.updateBook(3L, new BookDto("Clean Code: A Handbook of Agile Software Craftsmanship"));
        prettyPrint("Updated book", bookService.getBookById(3L));
    }

    private void prettyPrint(String title, Object object) {
        System.out.println(title + ":\n\t" + object + "\n");
    }

    private void prettyPrint(String title, List<?> objects) {
        String prettyList = objects.stream()
                .map(bookDto -> "\t\t" + bookDto.toString())
                .reduce((item1, item2) -> item1 + ",\n" + item2)
                .map(items -> "\t[\n" + items + "\n\t]")
                .orElse("[]");
        System.out.println(title + ":\n" + prettyList);
        System.out.println();
    }
}