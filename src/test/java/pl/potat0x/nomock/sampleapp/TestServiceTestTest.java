package pl.potat0x.nomock.sampleapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestServiceTestTest {

    private TestRepository testRepository = new InMemoryTestRepository();

    @Test
    public void name() {
        System.out.println(testRepository.save(new TestEntity(123L, "test")));
        System.out.println(testRepository.findById(123L));
    }
}
