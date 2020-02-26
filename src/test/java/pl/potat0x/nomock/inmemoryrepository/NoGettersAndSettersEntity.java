package pl.potat0x.nomock.inmemoryrepository;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
class NoGettersAndSettersEntity {

    @Id
    public Long id;

    public NoGettersAndSettersEntity(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoGettersAndSettersEntity that = (NoGettersAndSettersEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
