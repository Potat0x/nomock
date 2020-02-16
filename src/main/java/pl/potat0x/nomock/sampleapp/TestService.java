package pl.potat0x.nomock.sampleapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class TestService {
    private final TestRepository testRepository;
    private static int iter = 0;

    @Autowired
    TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
//        this.testRepository = new InMemoryTestRepository();
    }

    public Iterable<TestEntity> getAll() {
        return testRepository.findAll();
    }

    public void addNew() {
        TestEntity s = new TestEntity("test_" + ++iter);
        s.setId(10L);
        testRepository.save(s);
    }

    @Transactional
    public String rename(Long id) {
        return testRepository.findById(id)
                .map(testEnt -> {
                    testEnt.setName(testEnt.getName() + "_x");
//                    return testRepository.save(testEnt);
                    return testEnt;
                })
                .map(TestEntity::toString)
                .orElseGet(() -> "Entity not found");
    }
}
