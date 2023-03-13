package oasisbot24.oasisapi.controller;

import lombok.RequiredArgsConstructor;
import oasisbot24.oasisapi.domain.EmailDupCheckVO;
import oasisbot24.oasisapi.domain.SignInVO;
import oasisbot24.oasisapi.jwt.TokenDTO;
import oasisbot24.oasisapi.service.SignInService;
import oasisbot24.oasisapi.service.TestService;
import oasisbot24.oasisapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OasisController {

    private final TestService testService;
    private final SignInService signInService;
    private final UserService userService;

    @GetMapping("/test")
    public Map<String, Object> testController() {
        return testService.getTestData();
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenDTO> signInController(@RequestBody SignInVO signInVO) {
        return signInService.signIn(signInVO);
    }

    @GetMapping("/user")
    public Map<String, Object> userInfoController(@RequestHeader String data) {
        return userService.getUserData();
    }

    @PostMapping("/user")
    public ResponseEntity<EmailDupCheckVO> signUpController(@RequestBody Map<String, Object> payload) {
        return userService.signUpUser(payload);
    }

    @GetMapping("/signup")
    public String emailVerificationController(@RequestParam("email") String email, @RequestParam("auth") String auth) {
        return userService.emailVerification(email, auth);
    }
}
