package oasisbot24.oasisapi.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDTO {
    private String token;
    private String state;
    private String msg;
}
