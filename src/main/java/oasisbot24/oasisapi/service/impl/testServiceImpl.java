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

        testData.put("label1", "check1");
        testData.put("label2", "check2");
        testData.put("label3", "check3");
        testData.put("label4", "check4");

        return testData;
    }
}
