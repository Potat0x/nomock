package pl.potat0x.nomock.inmemoryrepository;

final class IdGenerator {
    private long id = 0;

    long nextId() {
        return ++id;
    }
}
