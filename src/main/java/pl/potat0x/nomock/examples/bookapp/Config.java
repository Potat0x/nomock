package pl.potat0x.nomock.examples.bookapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.potat0x.nomock.examples.repositories.CrudBookRepository;
import pl.potat0x.nomock.examples.repositories.InMemoryCrudBookRepository;

@Configuration
@Profile("in-memory-repository")
public class Config {

    @Bean
    public CrudBookRepository bookRepository() {
        return new InMemoryCrudBookRepository();
    }
}
