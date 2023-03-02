package oasisbot24.oasisapi.domain;

import lombok.Data;
import nonapi.io.github.classgraph.json.Id;

import java.time.LocalDateTime;

@Data
public class Transaction {

    @Id
    private Long id;

    private Long userId;
    private LocalDateTime infoCreateDate;
    private LocalDateTime date;
    private String type;
    private String position;
    private Double price;
    private Double volume;
    private Double totalPrice;
    private Double profitLossRate;
    private Double profitLoss;
    private Long point;
}
