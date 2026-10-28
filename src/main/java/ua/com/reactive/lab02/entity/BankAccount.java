package ua.com.reactive.lab02.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    private Long id;
    private String number;
    private BigDecimal balance;
    private BankAccountStatus status;
    private Long userId;
}
