package ua.com.reactive.lab02.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab02.entity.Message_test;
import ua.com.reactive.lab02.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Flux<Message_test> list() {
        return messageRepository.findAll();
    }

    public Mono<Message_test> addOne(Message_test message) {
        return messageRepository.save(message);
    }
}
