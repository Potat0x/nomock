package pl.potat0x.nomock.inmemoryrepository;

public final class InMemoryRepositoryException extends RuntimeException {

    public InMemoryRepositoryException(Throwable cause) {
        super(cause);
    }

    public InMemoryRepositoryException(String message) {
        super(message);
    }
}
