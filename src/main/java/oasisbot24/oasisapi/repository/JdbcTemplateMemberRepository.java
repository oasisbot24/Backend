package oasisbot24.oasisapi.repository;

import oasisbot24.oasisapi.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateMemberRepository implements MemberRepository{

    private final JdbcTemplate jdbcTemplate;

    //생성자가 한 개이기 때문에 Autowired 생략 가능
    public JdbcTemplateMemberRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("User").usingGeneratedKeyColumns("UserId");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("UserEmail", member.getEmail());
        parameters.put("UserPassword", member.getPassword());
        parameters.put("UserCreateDate", member.getCreateDate());
        parameters.put("UserUpdateDate", member.getUpdateDate());
        parameters.put("UserPhone", member.getPhone());
        parameters.put("UserNickName", member.getNickname());
        parameters.put("UserPoint", member.getPoint());
        parameters.put("UserCommissionRate", member.getCommissionRate());
        parameters.put("UserNft", member.getNft());
        parameters.put("UserType", member.getType());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        member.setId(key.longValue());
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        List<Member> result = jdbcTemplate.query("select * from User where UserId = ?", memberRowMapper(), id);
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        List<Member> result = jdbcTemplate.query("select * from User where UserEmail = ?", memberRowMapper(), email);
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("select * from User", memberRowMapper());
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("UserId"));
            member.setEmail(rs.getString("UserEmail"));
            member.setPassword(rs.getString("UserPassword"));
            member.setCreateDate(rs.getTimestamp("UserCreateDate").toLocalDateTime());
            member.setUpdateDate(rs.getTimestamp("UserUpdateDate").toLocalDateTime());
            member.setPhone(rs.getString("UserPhone"));
            member.setNickname(rs.getString("UserNickName"));
            member.setPoint(rs.getLong("UserPoint"));
            member.setCommissionRate(rs.getLong("UserCommissionRate"));
            member.setNft(rs.getLong("UserNft"));
            member.setType(rs.getInt("UserType"));
            return member;
        };
    }
}
