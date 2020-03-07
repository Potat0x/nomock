package pl.potat0x.nomock.inmemoryrepository.repository;

import java.util.UUID;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class IdGenerators {
    public static final Supplier<UUID> UuidGenerator = UUID::randomUUID;
    public static final UnaryOperator<Long> IncrementalLongIdGenerator = x -> x + 1;
    public static final UnaryOperator<Integer> IncrementalIntegerIdGenerator = x -> x + 1;
}
