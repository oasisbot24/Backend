package oasisbot24.oasisapi.service.impl;

import oasisbot24.oasisapi.service.SignInService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SignInServiceImpl implements SignInService {

    @Override
    public Map<String, Object> signIn() {

        Map<String, Object> signInData = new HashMap<>();

        return signInData;
    }
}
