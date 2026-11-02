package ua.com.reactive.lab.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab.entity.UserRole;

public interface UserRoleRepository extends ReactiveCrudRepository<UserRole, Long> {

    @Modifying
    @Query("DELETE FROM users_roles WHERE user_id = :userId")
    Mono<Integer> deleteAllByUserId(Long userId);
}
