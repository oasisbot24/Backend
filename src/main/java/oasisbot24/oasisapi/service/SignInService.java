package oasisbot24.oasisapi.service;

import oasisbot24.oasisapi.domain.SignInVO;
import oasisbot24.oasisapi.jwt.TokenDTO;
import org.springframework.http.ResponseEntity;

public interface SignInService {
    ResponseEntity<TokenDTO> signIn(SignInVO signInVO);
}
