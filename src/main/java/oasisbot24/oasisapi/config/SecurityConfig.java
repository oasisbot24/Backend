package oasisbot24.oasisapi.config;

import lombok.RequiredArgsConstructor;
import oasisbot24.oasisapi.jwt.JwtAccessDeniedHandler;
import oasisbot24.oasisapi.jwt.JwtAuthenticationEntryPoint;
import oasisbot24.oasisapi.jwt.JwtSecurityConfig;
import oasisbot24.oasisapi.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring()
                .antMatchers("/favicon.ico");
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                //401, 403 Exception 핸들링
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                //세션 사용하지 않음
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //HttpServletRequest를 사용하는 요청들에 대한 접근 제한 설정
                .and()
                .authorizeRequests()
                .antMatchers("/authenticate",
                        "/","/api/v1/test","/api/v1/user",
                        "/v2/api-docs",  "/configuration/ui",
                        "/swagger-resources/**", "/configuration/security",
                        "/swagger-ui/**", "/webjars/**","/swagger/**").permitAll()
                .anyRequest().authenticated()

                //JwtSecurityConfig 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }
}
