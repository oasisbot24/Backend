package oasisbot24.oasisapi.domain;

import lombok.Data;
import nonapi.io.github.classgraph.json.Id;

import java.time.LocalDateTime;

@Data
public class PointChangeInfo {

    @Id
    private Long id;

    private Long userId;
    private Long pointTopupId;
    private LocalDateTime createDate;
    private Long type;
    private String detail;
    private Long amount;
}
