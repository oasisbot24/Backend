package oasisbot24.oasisapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import oasisbot24.oasisapi.common.SignInMsg;
import oasisbot24.oasisapi.domain.SignInVO;
import oasisbot24.oasisapi.jwt.TokenDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class SignInServiceTest {

    @Autowired SignInService signInService;

    @Test
    void 로그인성공() {
        //given
        Map<String, Object> signInReq = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        signInReq.put("email","test@test.com");
        signInReq.put("password","test!");

        SignInVO signInVO = objectMapper.convertValue(signInReq, SignInVO.class);

        //when
        ResponseEntity<TokenDTO> isSignedIn = signInService.signIn(signInVO);

        //then
        assertThat(isSignedIn.getBody().getMsg()).isEqualTo(SignInMsg.SUCCESS);
    }

    @Test
    void 없는이메일로그인() {
        //given
        Map<String, Object> signInReq = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        signInReq.put("email","testt@test.com");
        signInReq.put("password","test!");

        SignInVO signInVO = objectMapper.convertValue(signInReq, SignInVO.class);

        //when
        ResponseEntity<TokenDTO> isSignedIn = signInService.signIn(signInVO);

        //then
        assertThat(isSignedIn.getBody().getMsg()).isEqualTo(SignInMsg.MISMATCH);
    }

    @Test
    void 틀린비밀번호로그인() {
        //given
        Map<String, Object> signInReq = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        signInReq.put("email","test@test.com");
        signInReq.put("password","test@");

        SignInVO signInVO = objectMapper.convertValue(signInReq, SignInVO.class);

        //when
        ResponseEntity<TokenDTO> isSignedIn = signInService.signIn(signInVO);

        //then
        assertThat(isSignedIn.getBody().getMsg()).isEqualTo(SignInMsg.MISMATCH);
    }

    @Test
    void 이메일미인증로그인() {
        //given
        Map<String, Object> signInReq = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        signInReq.put("email","nev@nev.com");
        signInReq.put("password","nev!!!!");

        SignInVO signInVO = objectMapper.convertValue(signInReq, SignInVO.class);

        //when
        ResponseEntity<TokenDTO> isSignedIn = signInService.signIn(signInVO);

        //then
        assertThat(isSignedIn.getBody().getMsg()).isEqualTo(SignInMsg.UNVERIFIED);
    }
}
