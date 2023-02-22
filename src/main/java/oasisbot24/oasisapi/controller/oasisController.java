package oasisbot24.oasisapi.controller;

import lombok.RequiredArgsConstructor;
import oasisbot24.oasisapi.service.testService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class oasisController {

    private final testService testService;

    @GetMapping("/test")
    public Map<String, Object> testController() {
        return testService.getTestData();
    }
}
