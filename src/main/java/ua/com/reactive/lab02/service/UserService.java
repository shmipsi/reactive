package ua.com.reactive.lab02.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab02.entity.Role;
import ua.com.reactive.lab02.entity.User;
import ua.com.reactive.lab02.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> findById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + id + " not found")));
    }

    public Mono<User> create(User user) {
        user.setId(null);
        if (user.getRole() == null) {
            user.setRole(Role.CLIENT);
        }
        return userRepository.save(user);
    }

    public Mono<User> update(Long id, User user) {
        return findById(id).flatMap(existing -> {
            existing.setEmail(user.getEmail());
            existing.setFirstName(user.getFirstName());
            existing.setLastName(user.getLastName());
            if (user.getPassword() != null) {
                existing.setPassword(user.getPassword());
            }
            if (user.getRole() != null) {
                existing.setRole(user.getRole());
            }
            return userRepository.save(existing);
        });
    }

    public Mono<Void> delete(Long id) {
        return findById(id).flatMap(userRepository::delete);
    }

    public Mono<User> requireAdmin(Long adminId) {
        return findById(adminId)
                .filter(user -> user.getRole() == Role.ADMIN)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "User " + adminId + " is not an administrator")));
    }
}
