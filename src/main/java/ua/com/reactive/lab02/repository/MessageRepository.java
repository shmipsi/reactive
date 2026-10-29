package ua.com.reactive.lab02.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ua.com.reactive.lab02.entity.Message_test;

public interface MessageRepository extends ReactiveCrudRepository <Message_test, Long> {
}
