package ua.com.reactive.lab02.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ua.com.reactive.lab02.entity.Payment;

public interface PaymentRepository extends ReactiveCrudRepository<Payment, Long> {
    @Query("SELECT * FROM payment WHERE giver_account_id = :accountId OR receiver_account_id = :accountId ORDER BY date DESC")
    Flux<Payment> findAllByAccountId(Long accountId);
}
