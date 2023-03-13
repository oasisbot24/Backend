package oasisbot24.oasisapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oasisbot24.oasisapi.common.MemberType;
import oasisbot24.oasisapi.common.SignInMsg;
import oasisbot24.oasisapi.common.SignInResult;
import oasisbot24.oasisapi.domain.Member;
import oasisbot24.oasisapi.domain.SignInVO;
import oasisbot24.oasisapi.jwt.JwtFilter;
import oasisbot24.oasisapi.jwt.TokenDTO;
import oasisbot24.oasisapi.jwt.TokenProvider;
import oasisbot24.oasisapi.repository.MemberRepository;
import oasisbot24.oasisapi.service.SignInService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignInServiceImpl implements SignInService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;

    @Override
    public ResponseEntity<TokenDTO> signIn(SignInVO signInVO) {

        Optional<Member> member = memberRepository.findByEmail(signInVO.getEmail());
        HttpHeaders httpHeaders = new HttpHeaders();

        if(member.isEmpty()) {
            return new ResponseEntity<>(new TokenDTO("", SignInResult.FAIL, SignInMsg.MISMATCH), httpHeaders, HttpStatus.OK);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInVO.getEmail(), signInVO.getPassword());

        //authenticationManagerBuilder가 호출되면서 CustomUserDetailService가 로드됨.
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            //이메일 인증 여부 판단
            try {
                validateVerifiedMember(member);
            } catch (Exception e) {
                return new ResponseEntity<>(new TokenDTO("", SignInResult.FAIL, SignInMsg.UNVERIFIED), httpHeaders, HttpStatus.OK);
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication);

            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

            return new ResponseEntity<>(new TokenDTO(jwt, SignInResult.SUCCESS, SignInMsg.SUCCESS), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            //비밀번호가 틀렸을 때
            log.info(member.get().getEmail() + " -> " + e.getMessage());
            return new ResponseEntity<>(new TokenDTO("", SignInResult.FAIL, SignInMsg.MISMATCH), httpHeaders, HttpStatus.OK);
        }
    }

    //userType이 0인지 아닌지 검사
    private void validateVerifiedMember(Optional<Member> member) {

         if(member.isPresent()) {
             Integer type = member.get().getType();
             if(Objects.equals(type, MemberType.UNVERIFIED)) {
                 log.info(member.get().getEmail() + " -> 아직 이메일 인증을 진행하지 않음.");
                 throw new IllegalStateException(member.get().getEmail()+" is unverified.");
             }
         }
    }
}