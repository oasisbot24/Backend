package oasisbot24.oasisapi.service;

public interface EmailVerificationService {
    String sendSimpleMessage(String to) throws Exception;
}
