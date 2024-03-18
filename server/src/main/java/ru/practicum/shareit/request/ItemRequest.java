package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "item_requests", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    // идентификатор:
    private Long id;

    // пользователь, написавший запрос:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    // описание запроса:
    @Column(name = "description")
    private String description;

    // время создания запроса:
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
}