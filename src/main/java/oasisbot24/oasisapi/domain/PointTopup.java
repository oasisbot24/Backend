package oasisbot24.oasisapi.domain;

import lombok.Data;
import nonapi.io.github.classgraph.json.Id;

import java.time.LocalDateTime;

@Data
public class PointTopup {

    @Id
    private Long id;

    private Long userId;
    private LocalDateTime createDate;
    private Long amount;
    private String tag;
    private Long condition;
}
