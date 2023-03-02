package oasisbot24.oasisapi.repository;

import oasisbot24.oasisapi.domain.EmailVerification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        parameters.put("EmailVerificationExpirationDate", emailVerification.getExpirationDate());
        parameters.put("EmailVerificationEmailAddress",emailVerification.getEmailAddress());
        parameters.put("EmailVerificationToken", emailVerification.getToken());
        parameters.put("EmailVerificationIsVerified", emailVerification.getIsVerified()?1:0);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        emailVerification.setId(key.longValue());
        return emailVerification;
    }

    @Override
    public Optional<EmailVerification> findById(Long id) {
        List<EmailVerification> result = jdbcTemplate.query("select * from EmailVerification where EmailVerificationId = ?", emailVerificationRowMapper(), id);
        return result.stream().findAny();
    }

    @Override
    public Optional<EmailVerification> findByEmail(String email) {
        List<EmailVerification> result = jdbcTemplate.query("select * from EmailVerification where EmailVerificationId = ?", emailVerificationRowMapper(), email);
        return result.stream().findAny();
    }

    private RowMapper<EmailVerification> emailVerificationRowMapper() {
        return (rs, rowNum) -> {
            EmailVerification emailVerification = new EmailVerification();
            emailVerification.setId(rs.getLong("EmailVerificationId"));
            emailVerification.setUserId(rs.getLong("UserId"));
            emailVerification.setExpirationDate(rs.getTimestamp("EmailVerificationExpirationDate").toLocalDateTime());
            emailVerification.setEmailAddress(rs.getString("EmailVerificationEmailAddress"));
            emailVerification.setToken(rs.getString("EmailVerificationToken"));
            emailVerification.setIsVerified(rs.getInt("EmailVerificationIsVerified")==1?Boolean.TRUE:Boolean.FALSE);
            return emailVerification;
        };
    }
}
