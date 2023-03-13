package oasisbot24.oasisapi.service;

import oasisbot24.oasisapi.common.IsEmailDuplicated;
import oasisbot24.oasisapi.domain.EmailDupCheckVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired UserService userService;

    @Test
    void 회원가입() {
        //given
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", "test@test.com");
        payload.put("password", "test!");
        payload.put("phone", "01012345678");
        payload.put("nick_name", "test");

        //when
        ResponseEntity<EmailDupCheckVO> isEmailSend = userService.signUpUser(payload);

        //then
        assertThat(isEmailSend.getBody().getIs_duplicated()).isEqualTo(IsEmailDuplicated.NO);
    }

    @Test
    void 이메일인증() {
        //given
        String email = "test@test.com";
        String auth = "8e6413a4-c61d-4540-94e1-19a65dda52a1";

        //when
        String isVerified = userService.emailVerification(email, auth);

        //then
        assertThat(isVerified).isEqualTo("이메일 인증이 완료되었습니다!");
    }
}
