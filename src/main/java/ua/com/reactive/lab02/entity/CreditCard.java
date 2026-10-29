package ua.com.reactive.lab02.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("credit_card")
public class CreditCard {
    @Id
    private Long id;
    private String number;
    private LocalDate expiryDate;
    private Long accountId;
}
