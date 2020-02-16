package pl.potat0x.nomock.inmemoryrepository;

final class InMemoryCrudRepositoryException extends RuntimeException {
    public InMemoryCrudRepositoryException(Throwable cause) {
        super(cause);
    }

    public InMemoryCrudRepositoryException(String message) {
        super(message);
    }
}
