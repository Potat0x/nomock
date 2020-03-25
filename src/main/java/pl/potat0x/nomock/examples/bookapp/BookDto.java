package pl.potat0x.nomock.examples.bookapp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

class BookDto {

    private final Long id;
    private final String name;
    private final String prop;

    @JsonCreator
    public BookDto(@JsonProperty("id") Long id, @JsonProperty("name") String name, @JsonProperty("prop") String prop) {
        this.id = id;
        this.name = name;
        this.prop = prop;
    }

    public BookDto(String name) {
        this.id = null;
        this.name = name;
        this.prop = null;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProp() {
        return prop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDto bookDto = (BookDto) o;
        return Objects.equals(id, bookDto.id) &&
                Objects.equals(name, bookDto.name) &&
                Objects.equals(prop, bookDto.prop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, prop);
    }

    @Override
    public String toString() {
        return "BookDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
