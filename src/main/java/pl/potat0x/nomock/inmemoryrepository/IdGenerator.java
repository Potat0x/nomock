package pl.potat0x.nomock.inmemoryrepository;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

final class IdGenerator<ID> {

    private ID nextId;
    private final UnaryOperator<ID> generator;

    IdGenerator(ID startId, UnaryOperator<ID> generator) {
        this.nextId = startId;

        this.generator = generator;
    }

    IdGenerator(Supplier<ID> generator) {
        this.nextId = generator.get();
        this.generator = id -> generator.get();
    }

    ID nextId() {
        ID id = nextId;
        nextId = generator.apply(nextId);
        return id;
    }
}
