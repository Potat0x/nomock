package pl.potat0x.nomock.examples.bookapp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public final class BookEntity {
    /*
    This class is used in examples - must be public.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String prop;

    public BookEntity() {
    }

    public BookEntity(Long id, String name, String prop) {
        this.id = id;
        this.name = name;
        this.prop = prop;
    }

    public BookEntity(String name, String prop) {
        this.name = name;
        this.prop = prop;
    }

    public BookEntity(String name) {
        this.name = name;
    }

    public BookEntity(Long id) {
        this.id = id;
    }

    public BookEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

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

    @Override
    public String toString() {
        return "BookEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prop='" + prop + '\'' +
                '}';
    }
}
