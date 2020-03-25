# Nomock
Spring Data JPA repositories backed by HashMap (alternative for mocks).

If you dont want to use [Testcontainers](https://www.testcontainers.org/)
or H2 database, its worth to considering in-memory repository instead of mocks.

## Features
- CrudRepository (fully supported)
- PagingAndSortingRepository (fully supported)
- JpaRepository ([queries by Example](https://www.baeldung.com/spring-data-query-by-example) not implemented)

## What are the benefits of using HashMap over repository mocks?
- to write tests, we dont have to investigate how service use repository
- service can be tested as blackbox
  - give input and verify output
  - refactor with ease

## Requirements
- entity must have ID field annotated with `@Id`

## Example

Simple Spring Boot app:

```java
@Service
class BookService {

    private final BookRepository bookRepository;

    @Autowired
    BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
```
```java
@Repository
interface BookRepository extends JpaRepository<BookEntity, Long> {
}
```
```java
@Entity
public final class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    //...

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookEntity that = (BookEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
```

How to test BookService using in-memory repository?
1. Create new [repository class](https://github.com/Potat0x/nomock/tree/master/src/test/java/pl/potat0x/nomock/examples/bookapp/InMemoryBookRepository.java), that
   - extends one of three available repositories: `InMemoryJpaRepository` or `InMemoryCrudRepository` or `InMemoryPagingAndSortingRepository`. In this case we use `InMemoryJpaRepository`
   - implements entity`s repository interface
```java
class InMemoryBookRepository extends InMemoryJpaRepository<BookEntity, Long> implements BookRepository {
    public InMemoryBookRepository() {
        super(1L, IdGenerators.IncrementalLongIdGenerator);
    }
}
```

2. Pass instance of this class to service: [BookServiceTest.java](https://github.com/Potat0x/nomock/tree/master/src/test/java/pl/potat0x/nomock/examples/bookapp/BookServiceTest.java)
```java
public class BookServiceTest {

    BookService bookService = new BookService(new InMemoryBookRepository());

    @Test
    public test() {
        //...
    }
}
```
3. Everything is ready to write tests

  
Check out entire code of this example:
 - [src/main/.../bookapp](https://github.com/Potat0x/nomock/tree/master/src/main/java/pl/potat0x/nomock/examples/bookapp)
 - [src/test/.../bookapp](https://github.com/Potat0x/nomock/tree/master/src/test/java/pl/potat0x/nomock/examples/bookapp)
