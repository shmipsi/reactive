package ua.com.reactive.lab02.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table("message")
public class Message_test {
    @Id
    private Long id;
    private String data;

    public Message_test(String data) {
        this.data = data;
    }
}
