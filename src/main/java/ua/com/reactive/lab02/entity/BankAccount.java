package ua.com.reactive.lab02.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("bank_account")
public class BankAccount {
    @Id
    private Long id;
    private String number;
    private BigDecimal balance;
    private BankAccountStatus status;
    private Long userId;
}
