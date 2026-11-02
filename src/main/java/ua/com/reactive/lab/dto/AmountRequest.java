package ua.com.reactive.lab.dto;

import java.math.BigDecimal;

public record AmountRequest(BigDecimal amount, String description) {
}
