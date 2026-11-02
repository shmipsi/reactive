package ua.com.reactive.lab.controler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab.entity.Message_test;
import ua.com.reactive.lab.service.MessageService_test;

@RestController
@RequestMapping("/controller")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MainController {

    private final MessageService_test messageService_test;

    @GetMapping
    public Flux<Message_test> list(
            @RequestParam(defaultValue = "0") Long start,
            @RequestParam(defaultValue = "3") Long count) {
        return messageService_test.list();
    }

    @PostMapping
    public Mono<Message_test> create(@RequestBody Message_test message) {
        return messageService_test.addOne(message);
    }
}