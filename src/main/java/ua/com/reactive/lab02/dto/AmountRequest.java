package ua.com.reactive.lab02.dto;

import java.math.BigDecimal;

public record AmountRequest(BigDecimal amount, String description) {
}
