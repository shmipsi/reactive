package ua.com.reactive.lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab.entity.Role;
import ua.com.reactive.lab.entity.User;
import ua.com.reactive.lab.entity.UserRole;
import ua.com.reactive.lab.repository.RoleRepository;
import ua.com.reactive.lab.repository.UserRepository;
import ua.com.reactive.lab.repository.UserRoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByEmail(username)
                .flatMap(this::withRoles);
    }

    public Flux<User> findAll() {
        return userRepository.findAll().concatMap(this::withRoles);
    }

    public Mono<User> findById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + id + " not found")))
                .flatMap(this::withRoles);
    }

    public Mono<User> register(User user) {
        user.setRoles(null);
        return create(user);
    }

    @Transactional
    public Mono<User> create(User user) {
        user.setId(null);
        Set<Role> requested = user.getRoles();
        Set<Role> roles = requested == null || requested.isEmpty() ? Set.of(Role.of(Role.CLIENT)) : requested;
        return resolveRoles(roles).flatMap(resolved -> {
            user.setPassword(encode(user.getPassword()));
            return userRepository.save(user).flatMap(saved -> assignRoles(saved, resolved));
        });
    }

    @Transactional
    public Mono<User> update(Long id, User user) {
        return findById(id).flatMap(existing -> {
            existing.setEmail(user.getEmail());
            existing.setFirstName(user.getFirstName());
            existing.setLastName(user.getLastName());
            if (user.getPassword() != null) {
                existing.setPassword(encode(user.getPassword()));
            }
            Set<Role> requested = user.getRoles();
            if (requested == null || requested.isEmpty()) {
                return userRepository.save(existing).flatMap(this::withRoles);
            }
            return requireCurrentAdmin()
                    .then(resolveRoles(requested))
                    .flatMap(resolved -> userRepository.save(existing)
                            .flatMap(saved -> userRoleRepository.deleteAllByUserId(saved.getId())
                                    .then(assignRoles(saved, resolved))));
        });
    }

    public Mono<Void> delete(Long id) {
        return findById(id).flatMap(userRepository::delete);
    }

    private Mono<User> withRoles(User user) {
        return roleRepository.findAllByUserId(user.getId())
                .collectList()
                .map(roles -> {
                    user.setRoles(new HashSet<>(roles));
                    return user;
                });
    }

    private Mono<List<Role>> resolveRoles(Set<Role> requested) {
        return Flux.fromIterable(requested)
                .concatMap(role -> roleRepository.findByName(role.getName())
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown role " + role.getName()))))
                .collectList();
    }

    private Mono<User> assignRoles(User user, List<Role> roles) {
        return Flux.fromIterable(roles)
                .concatMap(role -> userRoleRepository.save(new UserRole(null, user.getId(), role.getId())))
                .then(Mono.fromSupplier(() -> {
                    user.setRoles(new HashSet<>(roles));
                    return user;
                }));
    }

    private Mono<Void> requireCurrentAdmin() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication())
                .filter(auth -> auth.getAuthorities().stream().anyMatch(a -> Role.ADMIN.equals(a.getAuthority())))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Only an administrator can assign roles")))
                .then();
    }

    private String encode(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must not be empty");
        }
        return passwordEncoder.encode(rawPassword);
    }
}
