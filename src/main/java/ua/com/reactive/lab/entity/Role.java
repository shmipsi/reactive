package ua.com.reactive.lab.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table("roles")
public class Role implements GrantedAuthority {

    public static final String CLIENT = "ROLE_CLIENT";
    public static final String ADMIN = "ROLE_ADMIN";

    @Id
    private Long id;
    private String name;

    @JsonCreator
    public static Role of(String name) {
        return new Role(null, name);
    }

    @JsonValue
    @Override
    public String getAuthority() {
        return getName();
    }
}
