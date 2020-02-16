package pl.potat0x.nomock.sampleapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
class Controller {

    private final TestService testService;
    private static int iter = 0;

    Controller(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    String test() {
        return "test: " + testService.getAll();
    }

    @GetMapping("/add")
    String add() {
        testService.addNew();
        return "test: " + testService.getAll();
    }

    @GetMapping("/rename/{id}")
    String add(@PathVariable Long id) {
        return testService.rename(id);
    }
}
