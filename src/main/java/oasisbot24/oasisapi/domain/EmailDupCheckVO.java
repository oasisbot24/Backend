package oasisbot24.oasisapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailDupCheckVO {
    private Integer is_duplicated;
}
