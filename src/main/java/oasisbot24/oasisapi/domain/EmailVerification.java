package oasisbot24.oasisapi.domain;

import lombok.Data;
import nonapi.io.github.classgraph.json.Id;

import java.time.LocalDateTime;

@Data
public class EmailVerification {

    @Id
    private Long id;

    private Long userId;
    private LocalDateTime expirationDate;
    private String emailAddress;
    private String token;
    private Boolean isVerified;
}
