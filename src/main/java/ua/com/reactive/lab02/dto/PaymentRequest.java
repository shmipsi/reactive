package ua.com.reactive.lab02.dto;

import java.math.BigDecimal;

public record PaymentRequest(Long giverAccountId, Long receiverAccountId, BigDecimal amount, String description) {
}
