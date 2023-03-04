package oasisbot24.oasisapi.repository;

import oasisbot24.oasisapi.domain.EmailVerification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateEmailVerificationRepository implements EmailVerificationRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateEmailVerificationRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public EmailVerification save(EmailVerification emailVerification) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("EmailVerification").usingGeneratedKeyColumns("EmailVerificationId");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("UserId", emailVerification.getUserId());
        parameters.put("EmailVerificationIssuedDate", emailVerification.getIssuedDate());
        parameters.put("EmailVerificationEmailAddress",emailVerification.getEmailAddress());
        parameters.put("EmailVerificationToken", emailVerification.getToken());
        parameters.put("EmailVerificationIsVerified", emailVerification.getIsVerified()?1:0);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        emailVerification.setId(key.longValue());
        return emailVerification;
    }

    @Override
    public void updateEmailVerificationIsVerifiedByEmailVerification(String email) {
        jdbcTemplate.update("update EmailVerification set EmailVerificationIsVerified = 1 where EmailVerificationEmailAddress = ?", email);
    }

    @Override
    public void updateEmailVerificationToken(String email, String auth) {
        jdbcTemplate.update("update EmailVerification set EmailVerificationToken = ? where EmailVerificationEmailAddress = ?", auth, email);
        jdbcTemplate.update("update EmailVerification set EmailVerificationIssuedDate = ? where EmailVerificationEmailAddress = ?", LocalDateTime.now(), email);
    }

    @Override
    public Optional<EmailVerification> findById(Long id) {
        List<EmailVerification> result = jdbcTemplate.query("select * from EmailVerification where EmailVerificationId = ?", emailVerificationRowMapper(), id);
        return result.stream().findAny();
    }

    @Override
    public Optional<EmailVerification> findByEmail(String email) {
        List<EmailVerification> result = jdbcTemplate.query("select * from EmailVerification where EmailVerificationEmailAddress = ?", emailVerificationRowMapper(), email);
        return result.stream().findAny();
    }

    private RowMapper<EmailVerification> emailVerificationRowMapper() {
        return (rs, rowNum) -> {
            EmailVerification emailVerification = new EmailVerification();
            emailVerification.setId(rs.getLong("EmailVerificationId"));
            emailVerification.setUserId(rs.getLong("UserId"));
            emailVerification.setIssuedDate(rs.getTimestamp("EmailVerificationIssuedDate").toLocalDateTime());
            emailVerification.setEmailAddress(rs.getString("EmailVerificationEmailAddress"));
            emailVerification.setToken(rs.getString("EmailVerificationToken"));
            emailVerification.setIsVerified(rs.getInt("EmailVerificationIsVerified")==1?Boolean.TRUE:Boolean.FALSE);
            return emailVerification;
        };
    }
}
