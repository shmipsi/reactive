package ua.com.reactive.lab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab.entity.Message_test;
import ua.com.reactive.lab.repository.MessageRepository_test;

@Service
public class MessageService_test {

    private final MessageRepository_test messageRepository;

    @Autowired
    public MessageService_test(MessageRepository_test messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Flux<Message_test> list() {
        return messageRepository.findAll();
    }

    public Mono<Message_test> addOne(Message_test message) {
        return messageRepository.save(message);
    }
}
