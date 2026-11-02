package ua.com.reactive.lab.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab.entity.Role;
import ua.com.reactive.lab.entity.User;
import ua.com.reactive.lab.repository.BankAccountRepository;
import ua.com.reactive.lab.repository.CreditCardRepository;
import ua.com.reactive.lab.repository.PaymentRepository;

import java.util.Objects;
import java.util.stream.Stream;

@Component("access")
@RequiredArgsConstructor
public class AccessGuard {

    private final BankAccountRepository bankAccountRepository;
    private final CreditCardRepository creditCardRepository;
    private final PaymentRepository paymentRepository;

    public boolean isSelf(Authentication auth, Long userId) {
        return isAdmin(auth) || (userId != null && userId.equals(currentUserId(auth)));
    }

    public Mono<Boolean> ownsAccount(Authentication auth, Long accountId) {
        if (isAdmin(auth)) {
            return Mono.just(true);
        }
        if (accountId == null) {
            return Mono.just(false);
        }
        Long userId = currentUserId(auth);
        return bankAccountRepository.findById(accountId)
                .map(account -> userId.equals(account.getUserId()))
                .defaultIfEmpty(true);
    }

    public Mono<Boolean> ownsCard(Authentication auth, Long cardId) {
        if (isAdmin(auth)) {
            return Mono.just(true);
        }
        return creditCardRepository.findById(cardId)
                .flatMap(card -> ownsAccount(auth, card.getAccountId()))
                .defaultIfEmpty(true);
    }

    public Mono<Boolean> canUpdateCard(Authentication auth, Long cardId, Long newAccountId) {
        return ownsCard(auth, cardId)
                .zipWith(ownsAccount(auth, newAccountId), Boolean::logicalAnd);
    }

    public Mono<Boolean> ownsPayment(Authentication auth, Long paymentId) {
        if (isAdmin(auth)) {
            return Mono.just(true);
        }
        Long userId = currentUserId(auth);
        return paymentRepository.findById(paymentId)
                .flatMap(payment -> Flux.fromStream(Stream.of(payment.getGiverAccountId(), payment.getReceiverAccountId())
                                .filter(Objects::nonNull))
                        .flatMap(bankAccountRepository::findById)
                        .any(account -> userId.equals(account.getUserId())))
                .defaultIfEmpty(true);
    }

    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream().anyMatch(a -> Role.ADMIN.equals(a.getAuthority()));
    }

    private Long currentUserId(Authentication auth) {
        return ((User) auth.getPrincipal()).getId();
    }
}
