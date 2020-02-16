package pl.potat0x.nomock.inmemoryrepository

import spock.lang.Specification
import spock.lang.Unroll

class EntityRipperTest extends Specification {

    @Unroll
    def "Should detect if object has specified @Id annotated attribute"() {
        expect:
        EntityRipper.getEntityId(object) == expectedResult

        where:
        object                  | expectedResult
        "test"                  | Optional.empty()
        this                    | Optional.empty()
        new TypicalEntity(null) | Optional.empty()
        new TypicalEntity(123L) | Optional.of(123L)
    }

    @Unroll
    def "Should set value to @Id annotated attribute"() {
        when:
        EntityRipper.setEntityId(entity, id)

        then:
        entity == expectedEntity

        where:
        entity                  | id   | expectedEntity
        new TypicalEntity(null) | 123L | new TypicalEntity(123L)
        new TypicalEntity(99)   | 22L  | new TypicalEntity(22L)
    }
}
