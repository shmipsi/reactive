package ua.com.reactive.lab.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table("users_roles")
public class UserRole {

    @Id
    private Long id;
    private Long userId;
    private Long roleId;
}
