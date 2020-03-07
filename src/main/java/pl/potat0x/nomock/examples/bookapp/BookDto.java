package pl.potat0x.nomock.examples.bookapp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProp() {
        return prop;
    }
}
