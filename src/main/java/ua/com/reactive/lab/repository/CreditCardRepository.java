package ua.com.reactive.lab.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ua.com.reactive.lab.entity.CreditCard;

public interface CreditCardRepository extends ReactiveCrudRepository<CreditCard, Long> {
    Flux<CreditCard> findAllByAccountId(Long accountId);
}
