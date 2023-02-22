package oasisbot24.oasisapi.service.impl;

import oasisbot24.oasisapi.service.testService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class testServiceImpl implements testService {

    @Override
    public Map<String, Object> getTestData() {

        Map<String, Object> testData = new HashMap<>();

        testData.put("label1", "Hello!");
        testData.put("label2", "This is");
        testData.put("label3", "Oasis");
        testData.put("label4", "test API response!!!");

        return testData;
    }
}
