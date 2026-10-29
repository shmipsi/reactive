package ua.com.reactive.lab02.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab02.dto.AmountRequest;
import ua.com.reactive.lab02.dto.PaymentRequest;
import ua.com.reactive.lab02.entity.BankAccount;
import ua.com.reactive.lab02.entity.BankAccountStatus;
import ua.com.reactive.lab02.entity.Payment;
import ua.com.reactive.lab02.entity.PaymentStatus;
import ua.com.reactive.lab02.entity.PaymentType;
import ua.com.reactive.lab02.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BankAccountService bankAccountService;

    public Flux<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Flux<Payment> findByAccountId(Long accountId) {
        return bankAccountService.findById(accountId)
                .flatMapMany(account -> paymentRepository.findAllByAccountId(accountId));
    }

    public Mono<Payment> findById(Long id) {
        return paymentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment " + id + " not found")));
    }

    // amount, accounts and status of a payment are immutable; only the description can be corrected
    public Mono<Payment> update(Long id, Payment payment) {
        return findById(id).flatMap(existing -> {
            existing.setDescription(payment.getDescription());
            return paymentRepository.save(existing);
        });
    }

    public Mono<Void> delete(Long id) {
        return findById(id).flatMap(paymentRepository::delete);
    }

    @Transactional
    public Mono<Payment> refill(Long accountId, AmountRequest request) {
        return validateAmount(request.amount())
                .then(bankAccountService.findById(accountId))
                .flatMap(account -> {
                    Payment payment = newPayment(PaymentType.REFILL, null, accountId, request.amount(), request.description());
                    if (account.getStatus() == BankAccountStatus.BLOCKED) {
                        return fail(payment);
                    }
                    account.setBalance(account.getBalance().add(request.amount()));
                    return bankAccountService.save(account).then(succeed(payment));
                });
    }

    @Transactional
    public Mono<Payment> pay(PaymentRequest request) {
        if (request.giverAccountId() != null && request.giverAccountId().equals(request.receiverAccountId())) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giver and receiver accounts must differ"));
        }
        return validateAmount(request.amount())
                .then(bankAccountService.findById(request.giverAccountId()))
                .zipWith(bankAccountService.findById(request.receiverAccountId()))
                .flatMap(tuple -> {
                    BankAccount giver = tuple.getT1();
                    BankAccount receiver = tuple.getT2();
                    Payment payment = newPayment(PaymentType.PAYMENT, giver.getId(), receiver.getId(), request.amount(), request.description());
                    if (giver.getStatus() == BankAccountStatus.BLOCKED
                            || receiver.getStatus() == BankAccountStatus.BLOCKED
                            || giver.getBalance().compareTo(request.amount()) < 0) {
                        return fail(payment);
                    }
                    giver.setBalance(giver.getBalance().subtract(request.amount()));
                    receiver.setBalance(receiver.getBalance().add(request.amount()));
                    return bankAccountService.save(giver)
                            .then(bankAccountService.save(receiver))
                            .then(succeed(payment));
                });
    }

    private Mono<Void> validateAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive"));
        }
        return Mono.empty();
    }

    private Payment newPayment(PaymentType type, Long giverId, Long receiverId, BigDecimal amount, String description) {
        return new Payment(null, receiverId, giverId, amount, type, LocalDateTime.now(), description, null);
    }

    private Mono<Payment> succeed(Payment payment) {
        payment.setStatus(PaymentStatus.SUCCESS);
        return paymentRepository.save(payment);
    }

    // a rejected operation is still recorded, so the client can see it in the history
    private Mono<Payment> fail(Payment payment) {
        payment.setStatus(PaymentStatus.FAILED);
        return paymentRepository.save(payment);
    }
}
