package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.user.User;

import javax.persistence.*;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "items", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    // идентификатор:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    // название:
    @Column(name = "name")
    private String name;

    // описание:
    @Column(name = "description")
    private String description;

    // доступно для аренды или нет:
    @Column(name = "available")
    private Boolean available;

    // владелец предмета:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;
}