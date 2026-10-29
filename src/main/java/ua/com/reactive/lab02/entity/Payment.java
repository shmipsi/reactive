package ua.com.reactive.lab02.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("payment")
public class Payment {
    @Id
    private Long id;
    private Long receiverAccountId;
    private Long giverAccountId;
    private BigDecimal amount;
    private PaymentType type;
    private LocalDateTime date;
    private String description;
    private PaymentStatus status;
}
