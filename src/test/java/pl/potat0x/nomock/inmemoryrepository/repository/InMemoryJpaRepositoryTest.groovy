package pl.potat0x.nomock.inmemoryrepository.repository

import org.springframework.dao.EmptyResultDataAccessException
import pl.potat0x.nomock.examples.bookapp.BookEntity
import pl.potat0x.nomock.examples.bookapp.InMemoryJpaBookRepository
import pl.potat0x.nomock.examples.bookapp.JpaBookRepository
import spock.lang.Shared
import spock.lang.Specification

import javax.persistence.EntityNotFoundException

class InMemoryJpaRepositoryTest extends Specification {

    @Shared
    JpaBookRepository repository

    def setup() {
        repository = new InMemoryJpaBookRepository()
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
        given: "entities with null ID"
        def unsavedEntities = [new BookEntity("name1"), new BookEntity("name2")]

        when:
        def savedEntities = list(repository.saveAll(unsavedEntities))
        def identifiersOfFirstSavedGroup = savedEntities.collect { entity -> entity.id }

        then: "entities should be saved"
        repository.findAll().containsAll(savedEntities)

        and: "all entities should have assigned ID"
        savedEntities.every { entity -> entity.id != null }

        and: "all assigned IDs should be unique"
        savedEntities.collect { entity -> entity.id }.toSet().size() == 2

        when: "one element is modified and one is added"
        savedEntities.get(0).name = "new_name"
        savedEntities.add(new BookEntity("name3"))
        savedEntities = list(repository.saveAll(savedEntities))

        then: "repository should contains all created and updated objects"
        repository.findAll().containsAll(savedEntities)
        savedEntities.collect { entity -> entity.name }.containsAll("name2", "name3", "new_name")

        and: "all entities should have assigned IDs"
        savedEntities.every { entity -> entity.id != null }

        and: "all IDs should be unique"
        savedEntities.collect { entity -> entity.id }.toSet().size() == 3

        and: "IDs of initially saved entities should remain unchanged"
        savedEntities.collect { entity -> entity.getId() }.containsAll(identifiersOfFirstSavedGroup)
    }

    def "FindById"() {
        given:
        def savedEntities = repository.saveAll([new BookEntity("name1"), new BookEntity("name2")])

        expect:
        repository.findById(savedEntities[0].id).get().name == "name1"
        repository.findById(savedEntities[1].id).get().name == "name2"
    }

    def "ExistsById"() {
        given:
        def savedEntitiesIds = repository.saveAll([new BookEntity("name1"), new BookEntity("name2")])
                .collect { entity -> entity.id }

        expect:
        repository.existsById(savedEntitiesIds[0])
        repository.existsById(savedEntitiesIds[1])
        !repository.existsById(-1)
    }

    def "FindAll"() {
        given:
        repository.saveAll([new BookEntity("name1"), new BookEntity("name2")])

        when:
        def foundEntities = repository.findAll()

        then:
        foundEntities.collect { entity -> entity.name }.containsAll(["name1", "name2"])
    }

    def "FindAllById"() {
        given:
        def savedEntities = repository.saveAll([new BookEntity("name1"),
                                                new BookEntity("name2"),
                                                new BookEntity("name3"),
                                                new BookEntity("name4")])
        expect:
        repository.findAllById([-1, savedEntities[0].id, savedEntities[2].id, -1, -2]).size() == 2
    }

    def "FindAll and FindAllById should return empty collection (instead of null) when no entities found"() {
        expect:
        repository.findAll().size() == 0
        repository.findAllById([1, 2, 3]).size() == 0
    }

    def "Count"() {
        expect:
        repository.count() == 0

        when:
        repository.saveAll([new BookEntity("name"), new BookEntity("name")])

        then:
        repository.count() == 2
    }

    def "DeleteById"() {
        given:
        def savedEntities = repository.saveAll([new BookEntity("name1"), new BookEntity("name2"), new BookEntity("name3")])

        when: "one entity is deleted by id"
        repository.deleteById(savedEntities[1].id)

        then: "entity should not be present in repository"
        !(savedEntities[1] in repository.findAll())
        repository.findAll().containsAll(savedEntities[0], savedEntities[2])

        when: "entity to be deleted does not exists"
        repository.deleteById(-1)

        then:
        thrown EmptyResultDataAccessException
    }

    def "Delete"() {
        given:
        def savedEntities = repository.saveAll([new BookEntity("name1"), new BookEntity("name2"), new BookEntity("name3")])

        when: "one entity is deleted"
        repository.delete(savedEntities[1])

        and: "no exception should be thrown when entities to be deleted not exist"
        repository.delete(new BookEntity(-1, "name"))
        repository.delete(new BookEntity("name"))

        then: "entity should not be present in repository"
        !(savedEntities[1] in repository.findAll())
        repository.findAll().containsAll(savedEntities[0], savedEntities[2])
    }

    def "DeleteAll(entities)"() {
        given:
        def savedEntities = repository.saveAll([new BookEntity("name1"),
                                                new BookEntity("name2"),
                                                new BookEntity("name3"),
                                                new BookEntity("name4")])

        when: "two entities are deleted"
        repository.deleteAll([savedEntities[1], savedEntities[2]])

        then: "entity should not be present in repository"
        !(savedEntities[1] in repository.findAll())
        !(savedEntities[2] in repository.findAll())
        repository.findAll().containsAll(savedEntities[0], savedEntities[3])
    }

    def "DeleteAll"() {
        given:
        repository.saveAll([new BookEntity("name1"), new BookEntity("name2")])

        when:
        repository.deleteAll()

        then:
        repository.findAll().isEmpty()
    }

    def "GetOne"() {
        given:
        BookEntity savedEntity = repository.save(new BookEntity("name"))
        Long savedEntityId = savedEntity.getId()

        when:
        BookEntity fetchedEntity = repository.getOne(savedEntityId)

        then:
        fetchedEntity.getId() == savedEntity.getId()
        fetchedEntity.getName() == savedEntity.getName()

        when: "entity with given ID does not exists"
        repository.getOne(savedEntityId + 1)

        then:
        thrown EntityNotFoundException
    }

    def "Repository should thrown IllegalArgumentException when null is passed to repository method"() {
        when:
        methodCallWithNullArgument()

        then:
        thrown IllegalArgumentException

        where:
        methodCallWithNullArgument << [
                { -> repository.save(null) },
                { -> repository.saveAll(null) },
                { -> repository.saveAll([new BookEntity("name"), null]) },
                { -> repository.findById(null) },
                { -> repository.existsById(null) },
                { -> repository.findAllById(null) },
                { -> repository.findAllById([null]) },
                { -> repository.findAllById([1, 2, null]) },
                { -> repository.deleteById(null) },
                { -> repository.delete(null) },
                { -> repository.deleteAll([null]) },
                { -> repository.deleteAll([new BookEntity("name"), null]) },
                { -> repository.getOne(null) },
        ]
    }

    def "Should generate identifiers for saved objects"() {
        given:
        def unsavedBook1 = new BookEntity("book 1")
        def unsavedBook2 = new BookEntity("book 2")

        expect:
        unsavedBook1.id == null
        unsavedBook2.id == null

        when:
        def book1 = repository.save(unsavedBook1)
        def book2 = repository.save(unsavedBook2)

        then:
        book1.id != null && book2.id != null
        book1.id != book2.id
    }

    private static <T> List<T> list(Iterable<T> iterable) {
        List<T> list = new ArrayList<>()
        iterable.forEach { element -> list.add(element) }
        return list
    }
}
