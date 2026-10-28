package ua.com.reactive.lab02.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private Long id;
    private Long receiverAccountId;
    private Long giverAccountId;
    private BigDecimal amount;
    private PaymentType type;
    private Date date;
    private String description;
    private PaymentStatus status;
}
