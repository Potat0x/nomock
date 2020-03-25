package pl.potat0x.nomock.examples.bookapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class BookService {

    private final BookRepository bookRepository;

    @Autowired
    BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    List<BookDto> getAllBooks() {
        return getAllBooks(Sort.unsorted());
    }

    List<BookDto> getAllBooks(Sort sort) {
        return bookRepository.findAll(sort).stream()
                .map(this::bookEntityToDto)
                .collect(Collectors.toList());
    }

    Optional<BookDto> getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::bookEntityToDto);
    }

    BookDto createBook(BookDto book) {
        return bookEntityToDto(bookRepository.save(bookDtoToEntity(book)));
    }

    Optional<BookDto> updateBook(Long id, BookDto book) {
        BookEntity updatedBook = new BookEntity(id, book.getName(), book.getProp());
        return bookRepository.findById(id)
                .map(bookEntity -> bookRepository.save(updatedBook))
                .map(this::bookEntityToDto);
    }

    boolean deleteBook(Long id) {
        try {
            bookRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private BookDto bookEntityToDto(BookEntity bookEntity) {
        return new BookDto(bookEntity.getId(), bookEntity.getName(), bookEntity.getProp());
    }

    private BookEntity bookDtoToEntity(BookDto bookDto) {
        return new BookEntity(bookDto.getId(), bookDto.getName(), bookDto.getProp());
    }
}
