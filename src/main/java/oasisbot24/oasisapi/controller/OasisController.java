package oasisbot24.oasisapi.controller;

import lombok.RequiredArgsConstructor;
import oasisbot24.oasisapi.jwt.TokenProvider;
import oasisbot24.oasisapi.service.SignInService;
import oasisbot24.oasisapi.service.TestService;
import oasisbot24.oasisapi.service.UserService;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OasisController {

    private final TestService testService;
    private final SignInService signInService;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @GetMapping("/test")
    public Map<String, Object> testController() {
        return testService.getTestData();
    }

    @PostMapping("/signin")
    public Map<String, Object> signInController() {
        return signInService.signIn();
    }

    @GetMapping("/user")
    public Map<String, Object> userInfoController() {
        return userService.getUserData();
    }

    @PostMapping("/user")
    public void signUpController(@RequestBody Map<String, Object> payload) {
        userService.signUpUser(payload);
    }

    @GetMapping("/signup")
    public void emailVerificationController(@RequestParam("email") String email, @RequestParam("auth") String auth) {
        userService.emailVerification(email, auth);
    }
}
