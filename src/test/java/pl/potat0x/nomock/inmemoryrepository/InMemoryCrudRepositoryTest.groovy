package pl.potat0x.nomock.inmemoryrepository

import pl.potat0x.nomock.examples.bookapp.BookEntity
import pl.potat0x.nomock.examples.bookapp.BookRepository
import pl.potat0x.nomock.examples.bookapp.InMemoryBookRepository
import spock.lang.Specification

class InMemoryCrudRepositoryTest extends Specification {

    BookRepository repository

    def setup() {
        repository = new InMemoryBookRepository()
    }

    def "Save"() {
        given: "entity with null ID"
        def entity = new BookEntity("name")

        when:
        def savedEntityId = repository.save(entity).getId()

        then: "ID should be assigned to saved entity"
        savedEntityId != null

        and: "should be possible to find saved entity by ID"
        repository.findById(savedEntityId).get().getId() == savedEntityId
        repository.findById(savedEntityId).get().getName() == "name"

        when: "entity is updated and saved again"
        entity.setName("new name")
        def savedAgainEntityId = repository.save(entity).getId()

        then: "ID should not be modified"
        savedAgainEntityId == savedEntityId

        and: "entity in repository should be updated"
        repository.findById(savedEntityId).get().getId() == savedEntityId
        repository.findById(savedEntityId).get().getName() == "new name"
    }

    def "SaveAll"() {
    }

    def "FindById"() {
    }

    def "ExistsById"() {
    }

    def "FindAll"() {
    }

    def "FindAllById"() {
    }

    def "Count"() {
    }

    def "DeleteById"() {
    }

    def "Delete"() {
    }

    def "DeleteAll"() {
    }

    def "TestDeleteAll"() {
    }

    def "Should generate numeric ID for saved objects"() {
        given:
        def book1 = repository.save(new BookEntity("book 1"))
        def book2 = repository.save(new BookEntity("book 2"))

        expect:
        book1.id != book2.id
    }
}
