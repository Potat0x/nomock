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
        given:
        def entity = new BookEntity()

        when:
        println ""
        then:
        repository.save(entity) == entity
        repository.save(entity) == entity
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
}
