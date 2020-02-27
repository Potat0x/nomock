package pl.potat0x.nomock.inmemoryrepository

import org.mockito.internal.util.collections.Sets
import spock.lang.Specification

class IdGeneratorTest extends Specification {
    def "Should generate Integer identifiers via successor function"() {
        given:
        IdGenerator<Integer> idGenerator = new IdGenerator<>(100, { id -> id + 20 })

        expect:
        idGenerator.nextId() == 100
        idGenerator.nextId() == 120
        idGenerator.nextId() == 140
    }

    def "Should generate UUID identifiers via supplier"() {
        given:
        IdGenerator<UUID> idGenerator = new IdGenerator<>({ -> UUID.randomUUID() })

        expect:
        Sets.newSet(idGenerator.nextId(), idGenerator.nextId(), idGenerator.nextId()).size() == 3
    }
}
