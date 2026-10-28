package ua.com.reactive.lab02.controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ua.com.reactive.lab02.entity.Client_test;

@RestController
public class MyController{

    @GetMapping("/clients")

    public Flux<Client_test> getClients() {
        Flux<Client_test> clients = Flux.just(
                        new Client_test(1L, "Vasya", "Pypkin", 18),
                        new Client_test(2L, "Iva", "Pypkina", 19),
                        new Client_test(3L, "Inna", "Pypkina", 20)
                )
                .skip(0)
                .take(2);

        return clients;
    }
}
