package oasisbot24.oasisapi.jwt;

import lombok.extern.slf4j.Slf4j;
import oasisbot24.oasisapi.common.MemberType;
import oasisbot24.oasisapi.domain.Member;
import oasisbot24.oasisapi.repository.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException(email + " -> 사용자가 존재하지 않습니다."));

            log.info("Success find member {}", member);

            return User.builder()
                    .username(member.getEmail())
                    .password(member.getPassword())
                    //.authorities(Collections.emptyList()) //추후 authorities 관련 설정 추가
                    .roles(member.getType() == MemberType.ADMIN ? "ADMIN" : "USER")
                    .build();
    }
}
