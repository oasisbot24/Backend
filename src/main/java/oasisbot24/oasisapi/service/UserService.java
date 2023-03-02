package oasisbot24.oasisapi.service;

import java.util.Map;

public interface UserService {
    Map<String, Object> getUserData();

    void signUpUser(Map<String, Object> payload);

    void emailVerification(String email, String auth);
}
