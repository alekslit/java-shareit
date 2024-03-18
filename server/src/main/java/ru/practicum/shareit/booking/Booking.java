package ru.practicum.shareit.booking;

import lombok.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "bookings", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    // идентификатор:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;

    // статус бронирования:
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;

    // начало бронирования:
    @Column(name = "start_date")
    private LocalDateTime start;

    // конец бронирования:
    @Column(name = "end_date")
    private LocalDateTime end;

    // пользователь, который берёт предмет в аренду:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    // предмет, который хотят взять в аренду:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @ToString.Exclude
    private Item item;
}