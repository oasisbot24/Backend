package oasisbot24.oasisapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oasisbot24.oasisapi.common.IsEmailDuplicated;
import oasisbot24.oasisapi.common.MemberType;
import oasisbot24.oasisapi.domain.EmailDupCheckVO;
import oasisbot24.oasisapi.domain.EmailVerification;
import oasisbot24.oasisapi.domain.Member;
import oasisbot24.oasisapi.repository.EmailVerificationRepository;
import oasisbot24.oasisapi.repository.MemberRepository;
import oasisbot24.oasisapi.service.EmailVerificationService;
import oasisbot24.oasisapi.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final MemberRepository memberRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailVerificationService emailVerificationService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public static final int EMAIL_VERIFICATION_EXPIRATION_PERIOD = 2*24*60*60;

    @Override
    public Map<String, Object> getUserData() {

        Map<String, Object> userData = new HashMap<>();

        return userData;
    }

    @Override
    public ResponseEntity<EmailDupCheckVO> signUpUser(Map<String, Object> payload) {
        Member member = new Member();
        EmailVerification emailVerification = new EmailVerification();
        HttpHeaders httpHeaders = new HttpHeaders();

        member.setEmail(payload.get("email").toString()); //프론트에서 이메일형식 체크
        member.setPassword(payload.get("password").toString());
        member.encodePassword(bCryptPasswordEncoder);
        member.setPassword("{bcrypt}"+member.getPassword());
        member.setCreateDate(LocalDateTime.now());
        member.setUpdateDate(member.getCreateDate());
        member.setPhone(payload.get("phone").toString()); //프론트에서 대시 제거
        member.setNickname(payload.get("nick_name").toString());
        member.setPoint(0L);
        member.setCommissionRate(10L);
        member.setNft(0L);
        member.setType(MemberType.UNVERIFIED);

        try {
            validateDuplicateMember(member);
        } catch (Exception e) {
            return new ResponseEntity<>(new EmailDupCheckVO(IsEmailDuplicated.YES), httpHeaders, HttpStatus.EXPECTATION_FAILED);
        }

        memberRepository.save(member);

        //유저 이메일로 인증 메일 전송
        try {
            String token = emailVerificationService.sendSimpleMessage(member.getEmail());

            emailVerification.setUserId(member.getId());
            emailVerification.setIssuedDate(LocalDateTime.now());
            emailVerification.setEmailAddress(member.getEmail());
            emailVerification.setToken(token);
            emailVerification.setIsVerified(Boolean.FALSE);

            emailVerificationRepository.save(emailVerification);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(new EmailDupCheckVO(IsEmailDuplicated.NO), httpHeaders, HttpStatus.OK);
    }

    @Override
    public String emailVerification(String email, String auth) {
        Optional<EmailVerification> emailVerificationRes = emailVerificationRepository.findByEmail(email);
        String state;

        if(emailVerificationRes.isPresent()) {
            String userEmail = emailVerificationRes.get().getEmailAddress();

            if(Objects.equals(emailVerificationRes.get().getToken(), auth)) {
                LocalDateTime issuedDateTime = emailVerificationRes.get().getIssuedDate();
                Duration duration = Duration.between(issuedDateTime, LocalDateTime.now());

                if (duration.getSeconds() <= EMAIL_VERIFICATION_EXPIRATION_PERIOD) {
                    memberRepository.updateUserTypeByEmailVerification(userEmail);
                    emailVerificationRepository.updateEmailVerificationIsVerifiedByEmailVerification(userEmail);
                    state = "이메일 인증이 완료되었습니다!";
                } else {
                    log.info(email + " 사용자의 이메일 인증 토큰 기한이 만료되었습니다! 새로운 인증 이메일을 발송합니다.");

                    try {
                        String token = emailVerificationService.sendSimpleMessage(userEmail);
                        emailVerificationRepository.updateEmailVerificationToken(userEmail, token);
                        state = "사용자의 이메일 인증 토큰 기한이 만료되어 새로운 인증 이메일을 발송합니다.";
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                log.info(email + "사용자의 이메일 인증 토큰 정보가 잘못되었습니다!");
                state = "사용자의 이메일 인증 토큰 정보가 잘못되었습니다!";
            }
        } else {
            log.info(email + "사용자의 이메일 인증 토큰 정보를 찾을 수 없습니다!");
            state = "사용자의 이메일 인증 토큰 정보를 찾을 수 없습니다!";
        }

        return state;
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
}
