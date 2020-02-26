package pl.potat0x.nomock.examples.bookapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("in-memory-repository")
public class Config {

    @Bean
    public BookRepository bookRepository() {
        return new InMemoryBookRepository();
    }
}
