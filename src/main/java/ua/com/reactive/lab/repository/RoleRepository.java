package ua.com.reactive.lab.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab.entity.Role;

public interface RoleRepository extends ReactiveCrudRepository<Role, Long> {

    Mono<Role> findByName(String name);

    @Query("SELECT r.* FROM roles r JOIN users_roles ur ON ur.role_id = r.id WHERE ur.user_id = :userId")
    Flux<Role> findAllByUserId(Long userId);
}
