package oasisbot24.oasisapi.controller;

import lombok.RequiredArgsConstructor;
import oasisbot24.oasisapi.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OasisController {

    private final TestService testService;

    @GetMapping("/api/test")
    public Map<String, Object> testController() {
        return testService.getTestData();
    }
}
