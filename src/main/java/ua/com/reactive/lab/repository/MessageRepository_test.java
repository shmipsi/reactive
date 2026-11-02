package ua.com.reactive.lab.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ua.com.reactive.lab.entity.Message_test;

public interface MessageRepository_test extends ReactiveCrudRepository <Message_test, Long> {
}
