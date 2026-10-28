package ua.com.reactive.lab02.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {
    private Long id;
    private String number;
    private Date expiryDate;
    private Long accountId;
}
