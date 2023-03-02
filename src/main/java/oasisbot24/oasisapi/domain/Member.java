package oasisbot24.oasisapi.domain;

import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Data
public class Member {

    @Id
    private Long id;

    private String email;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String phone;
    private String nickname;
    private Long point;
    private Long commissionRate;
    private Long nft;
    private Integer type;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public Boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(plainPassword, this.password);
    }
}
