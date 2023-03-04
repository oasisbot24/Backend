package oasisbot24.oasisapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oasisbot24.oasisapi.domain.EmailVerification;
import oasisbot24.oasisapi.domain.Member;
import oasisbot24.oasisapi.repository.EmailVerificationRepository;
import oasisbot24.oasisapi.repository.MemberRepository;
import oasisbot24.oasisapi.service.EmailVerificationService;
import oasisbot24.oasisapi.service.UserService;
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
    public static final int EXPIRATION_PERIOD = 2*24*60*60;

    @Override
    public Map<String, Object> getUserData() {

        Map<String, Object> userData = new HashMap<>();

        return userData;
    }

    @Override
    public void signUpUser(Map<String, Object> payload) {
        Member member = new Member();
        EmailVerification emailVerification = new EmailVerification();

        member.setEmail(payload.get("email").toString()); //프론트에서 이메일형식 체크
        member.setPassword(payload.get("password").toString());
        member.encodePassword(bCryptPasswordEncoder);
        member.setCreateDate(LocalDateTime.now());
        member.setUpdateDate(member.getCreateDate());
        member.setPhone(payload.get("phone").toString()); //프론트에서 대시 제거
        member.setNickname(payload.get("nick_name").toString());
        member.setPoint(0L);
        member.setCommissionRate(10L);
        member.setNft(0L);
        member.setType(0);

        validateDuplicateMember(member);
        memberRepository.save(member);

        //유저 이메일로 인증 메일 전송
        try {
            String Token = emailVerificationService.sendSimpleMessage(member.getEmail());

            emailVerification.setUserId(member.getId());
            emailVerification.setIssuedDate(LocalDateTime.now());
            emailVerification.setEmailAddress(member.getEmail());
            emailVerification.setToken(Token);
            emailVerification.setIsVerified(Boolean.FALSE);

            emailVerificationRepository.save(emailVerification);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void emailVerification(String email, String auth) {
        Optional<EmailVerification> emailVerificationRes = emailVerificationRepository.findByEmail(email);

        if(emailVerificationRes.isPresent()) {
            if(Objects.equals(emailVerificationRes.get().getToken(), auth)) {
                LocalDateTime issuedDateTime = emailVerificationRes.get().getIssuedDate();
                Duration duration = Duration.between(issuedDateTime, LocalDateTime.now());

                if (duration.getSeconds() <= EXPIRATION_PERIOD) {
                    memberRepository.updateUserTypeByEmailVerification(emailVerificationRes.get().getEmailAddress());
                    emailVerificationRepository.updateEmailVerificationIsVerifiedByEmailVerification(emailVerificationRes.get().getEmailAddress());
                } else {
                    log.info(email + " 사용자의 이메일 인증 토큰 기한이 만료되었습니다!");
                }
            } else {
                log.info(email + "사용자의 이메일 인증 토큰 정보가 잘못되었습니다!");
            }
        } else {
            log.info(email + "사용자의 이메일 인증 토큰 정보를 찾을 수 없습니다!");
        }
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
}
