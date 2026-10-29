package ua.com.reactive.lab02.controler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab02.dto.PaymentRequest;
import ua.com.reactive.lab02.entity.Payment;
import ua.com.reactive.lab02.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public Flux<Payment> list() {
        return paymentService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Payment> get(@PathVariable Long id) {
        return paymentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Payment> pay(@RequestBody PaymentRequest request) {
        return paymentService.pay(request);
    }

    @PutMapping("/{id}")
    public Mono<Payment> update(@PathVariable Long id, @RequestBody Payment payment) {
        return paymentService.update(id, payment);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return paymentService.delete(id);
    }
}
