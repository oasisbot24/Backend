package oasisbot24.oasisapi.service;

import oasisbot24.oasisapi.domain.EmailDupCheckVO;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {
    Map<String, Object> getUserData();

    ResponseEntity<EmailDupCheckVO> signUpUser(Map<String, Object> payload);

    String emailVerification(String email, String auth);
}
