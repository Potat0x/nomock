package pl.potat0x.nomock.inmemoryrepository;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
class TypicalEntity {

    @Id
    private Long id;

    TypicalEntity(Long id) {
        this.id = id;
    }

    void setId(Long id) {
        this.id = id;
    }

    Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TypicalEntity{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypicalEntity that = (TypicalEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
