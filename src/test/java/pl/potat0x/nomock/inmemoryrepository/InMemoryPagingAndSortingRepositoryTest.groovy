package pl.potat0x.nomock.inmemoryrepository

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import pl.potat0x.nomock.examples.bookapp.BookEntity
import pl.potat0x.nomock.examples.bookapp.InMemoryPagingAndSortingBookRepository
import pl.potat0x.nomock.examples.bookapp.PagingAndSortingBookRepository
import spock.lang.Specification

class InMemoryPagingAndSortingRepositoryTest extends Specification {

    def "FindAll(Pageable)"() {
        given:
        PagingAndSortingBookRepository repository = new InMemoryPagingAndSortingBookRepository()

        def books = [
                new BookEntity("b0"),
                new BookEntity("b1"),
                new BookEntity("b2"),
                new BookEntity("b3"),
                new BookEntity("b4"),
                new BookEntity("b5"),
                new BookEntity("b6"),
                new BookEntity("b7")
        ]
        repository.saveAll(books)

        when:
        def pageContent = repository.findAll(pageRequest)

        then:
        pageContent.size() == expectedBooksOnPage.size()
        pageContent.collect({ x -> x.name }).containsAll(expectedBooksOnPage)

        where:
        pageRequest              | expectedBooksOnPage
        PageRequest.of(0, 3)     | ["b0", "b1", "b2"]
        PageRequest.of(1, 3)     | ["b3", "b4", "b5"]
        PageRequest.of(2, 3)     | ["b6", "b7"]
        PageRequest.of(2, 3)     | ["b6", "b7"]
        PageRequest.of(0, 100)   | ["b0", "b1", "b2", "b3", "b4", "b5", "b6", "b7"]
        PageRequest.of(100, 100) | []
        PageRequest.of(0, 1)     | ["b0"]
        PageRequest.of(7, 1)     | ["b7"]
        PageRequest.unpaged()    | ["b0", "b1", "b2", "b3", "b4", "b5", "b6", "b7"]
    }

    def "FindAll(Sort)"() {
        given:
        PagingAndSortingBookRepository repository = new InMemoryPagingAndSortingBookRepository()

        def books = [
                new BookEntity(null, null),
                new BookEntity("a", "x"),
                new BookEntity("c", "x"),
                new BookEntity("b", "z"),
                new BookEntity(null, null),
                new BookEntity("b", "x"),
                new BookEntity("b", null),
                new BookEntity("a", null),
                new BookEntity("c", "y"),
                new BookEntity(null, "y"),
                new BookEntity("d", "y"),
                new BookEntity("d", "x"),
                new BookEntity("a", null),
        ]
        repository.saveAll(books)

        when:
        def sortedBooks = repository.findAll(sort)

        then: "objects should be sorted by multiple fields"
        sortedBooks.size() == expectedBooks.size()
        assertBooksFieldsAreEqual(sortedBooks, expectedBooks)

        where:
        sort                                                     | expectedBooks
        Sort.by(Sort.Order.desc("prop"), Sort.Order.asc("name")) | [new BookEntity("b", "z"),
                                                                    new BookEntity(null, "y"),
                                                                    new BookEntity("c", "y"),
                                                                    new BookEntity("d", "y"),
                                                                    new BookEntity("a", "x"),
                                                                    new BookEntity("b", "x"),
                                                                    new BookEntity("c", "x"),
                                                                    new BookEntity("d", "x"),
                                                                    new BookEntity(null, null),
                                                                    new BookEntity(null, null),
                                                                    new BookEntity("a", null),
                                                                    new BookEntity("a", null),
                                                                    new BookEntity("b", null)]

        Sort.by(Sort.Order.asc("prop"), Sort.Order.asc("name"))  | [new BookEntity(null, null),
                                                                    new BookEntity(null, null),
                                                                    new BookEntity("a", null),
                                                                    new BookEntity("a", null),
                                                                    new BookEntity("b", null),
                                                                    new BookEntity("a", "x"),
                                                                    new BookEntity("b", "x"),
                                                                    new BookEntity("c", "x"),
                                                                    new BookEntity("d", "x"),
                                                                    new BookEntity(null, "y"),
                                                                    new BookEntity("c", "y"),
                                                                    new BookEntity("d", "y"),
                                                                    new BookEntity("b", "z")]

        Sort.by(Sort.Direction.ASC, "name", "prop")              | [new BookEntity(null, null),
                                                                    new BookEntity(null, null),
                                                                    new BookEntity(null, "y"),
                                                                    new BookEntity("a", null),
                                                                    new BookEntity("a", null),
                                                                    new BookEntity("a", "x"),
                                                                    new BookEntity("b", null),
                                                                    new BookEntity("b", "x"),
                                                                    new BookEntity("b", "z"),
                                                                    new BookEntity("c", "x"),
                                                                    new BookEntity("c", "y"),
                                                                    new BookEntity("d", "x"),
                                                                    new BookEntity("d", "y")]

        Sort.by(Sort.Order.asc("prop"), Sort.Order.desc("name")) | [new BookEntity("b", null),
                                                                    new BookEntity("a", null),
                                                                    new BookEntity("a", null),
                                                                    new BookEntity(null, null),
                                                                    new BookEntity(null, null),
                                                                    new BookEntity("d", "x"),
                                                                    new BookEntity("c", "x"),
                                                                    new BookEntity("b", "x"),
                                                                    new BookEntity("a", "x"),
                                                                    new BookEntity("d", "y"),
                                                                    new BookEntity("c", "y"),
                                                                    new BookEntity(null, "y"),
                                                                    new BookEntity("b", "z")]
    }

    def "FindAll(Sort) case sensitive"() {
        given:
        PagingAndSortingBookRepository repository = new InMemoryPagingAndSortingBookRepository()

        def books = [
                new BookEntity("B", "d"),
                new BookEntity("a", "D"),
                new BookEntity("b", "D"),
                new BookEntity(null, null),
                new BookEntity("A", "c"),
                new BookEntity("B", null),
                new BookEntity("b", "d"),
        ]
        repository.saveAll(books)

        when:
        def sortedBooks = repository.findAll(sort)

        then: "objects should be sorted by multiple fields"
        sortedBooks.size() == expectedBooks.size()
        assertBooksFieldsAreEqual(sortedBooks, expectedBooks)

        where:
        sort                                         | expectedBooks
        Sort.by(Sort.Order.asc("name"),
                Sort.Order.asc("prop"))              | [new BookEntity(null, null),
                                                        new BookEntity("A", "c"),
                                                        new BookEntity("B", null),
                                                        new BookEntity("B", "d"),
                                                        new BookEntity("a", "D"),
                                                        new BookEntity("b", "D"),
                                                        new BookEntity("b", "d")]
        Sort.by(Sort.Order.asc("name").ignoreCase(),
                Sort.Order.asc("prop").ignoreCase()) | [new BookEntity(null, null),
                                                        new BookEntity("A", "c"),
                                                        new BookEntity("a", "D"),
                                                        new BookEntity("B", null),
                                                        new BookEntity("B", "d"),
                                                        new BookEntity("b", "D"),
                                                        new BookEntity("b", "d")]
        Sort.by(Sort.Order.desc("name").ignoreCase(),
                Sort.Order.desc("prop"))             | [new BookEntity("B", "d"),
                                                        new BookEntity("b", "d"),
                                                        new BookEntity("b", "D"),
                                                        new BookEntity("B", null),
                                                        new BookEntity("A", "c"),
                                                        new BookEntity("a", "D"),
                                                        new BookEntity(null, null)]
    }

    def "Should combine paging and sorting"() {
        given:
        PagingAndSortingBookRepository repository = new InMemoryPagingAndSortingBookRepository()

        def books = [
                new BookEntity("b3"),
                new BookEntity("b0"),
                new BookEntity("b2"),
                new BookEntity("b1"),
                new BookEntity("b4"),
                new BookEntity("b2"),
        ]
        repository.saveAll(books)

        when:
        def sortedPageContent = repository.findAll(request)

        then:
        sortedPageContent.size() == expectedBooksOnPage.size()
        for (int i = 0; i < sortedPageContent.size(); i++) {
            assert sortedPageContent[i].name == expectedBooksOnPage[i]
        }

        where:
        request                                             | expectedBooksOnPage
        PageRequest.of(0, 100, Sort.by("name").ascending()) | ["b0", "b1", "b2", "b2", "b3", "b4"]
        PageRequest.of(0, 4, Sort.unsorted())               | ["b3", "b0", "b2", "b1"]
        PageRequest.of(0, 3, Sort.by("name").ascending())   | ["b0", "b1", "b2"]
        PageRequest.of(1, 3, Sort.by("name").ascending())   | ["b2", "b3", "b4"]
        PageRequest.of(1, 4, Sort.Direction.DESC, "name")   | ["b1", "b0"]
    }

    def "Should sort entities by numeric field"() {
        given:
        PagingAndSortingBookRepository repository = new InMemoryPagingAndSortingBookRepository()
        repository.saveAll([new BookEntity("b0"), new BookEntity("b1"), new BookEntity("b2")])

        when:
        def sortedBooks = repository.findAll(sort)

        then:
        sortedBooks.collect({ entity -> entity.name }) == expectedBooksOrder

        where:
        sort                       | expectedBooksOrder
        Sort.by("id").ascending()  | ["b0", "b1", "b2"]
        Sort.by("id").descending() | ["b2", "b1", "b0"]
    }

    def "Should sort entities respecting null policy"() {
        given:
        PagingAndSortingBookRepository repository = new InMemoryPagingAndSortingBookRepository()
        repository.saveAll([
                new BookEntity("b0"),
                new BookEntity(null),
                new BookEntity("b1"),
                new BookEntity("b2"),
                new BookEntity(null)
        ])

        when:
        def sortedBooks = repository.findAll(sort)

        then:
        sortedBooks.collect({ entity -> entity.name }) == expectedBooksOrder

        where:
        sort                                          | expectedBooksOrder
        Sort.by("name").ascending()                   | [null, null, "b0", "b1", "b2"]
        Sort.by("name").descending()                  | ["b2", "b1", "b0", null, null]
        Sort.by(Sort.Order.asc("name").nullsFirst())  | [null, null, "b0", "b1", "b2"]
        Sort.by(Sort.Order.asc("name").nullsLast())   | ["b0", "b1", "b2", null, null]
        Sort.by(Sort.Order.desc("name").nullsFirst()) | [null, null, "b2", "b1", "b0"]
        Sort.by(Sort.Order.desc("name").nullsLast())  | ["b2", "b1", "b0", null, null]
    }

    void assertBooksFieldsAreEqual(Iterable<BookEntity> result, List<BookEntity> expected) {
        for (int i = 0; i < result.size(); i++) {
            assert result[i].name == expected[i].name
            assert result[i].prop == expected[i].prop
        }
    }
}
