package pl.potat0x.nomock.inmemoryrepository

import pl.potat0x.nomock.examples.bookapp.BookEntity
import spock.lang.Specification
import spock.lang.Unroll

class EntityRipperTest extends Specification {

    @Unroll
    def "Should detect if object has specified @Id annotated field"() {
        expect:
        EntityRipper.getEntityId(object) == expectedResult

        where:
        object                              | expectedResult
        "test"                              | Optional.empty()
        new BookEntity(null)                | Optional.empty()
        new BookEntity(123L)                | Optional.of(123L)
        new NoGettersAndSettersEntity(null) | Optional.empty()
        new NoGettersAndSettersEntity(123L) | Optional.of(123L)
    }

    @Unroll
    def "Should set value to @Id annotated field"() {
        when:
        EntityRipper.setEntityId(entity, id)

        then:
        entity == expectedEntity

        where:
        entity                              | id   | expectedEntity
        new BookEntity(null)                | 123L | new BookEntity(123L)
        new BookEntity(99)                  | 22L  | new BookEntity(22L)
        new NoGettersAndSettersEntity(null) | 123L | new NoGettersAndSettersEntity(123L)
        new NoGettersAndSettersEntity(99)   | 22L  | new NoGettersAndSettersEntity(22L)
    }

    def "Should throw custom exception while attempting to assign id to object with no @Id annotated field"() {
        when:
        EntityRipper.setEntityId("this is not entity", 123L)

        then:
        thrown InMemoryCrudRepositoryException
    }
}
