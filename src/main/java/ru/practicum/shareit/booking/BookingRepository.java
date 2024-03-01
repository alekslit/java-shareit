package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByUserIdOrderByStartDesc(Long userId);

    List<Booking> findAllByItemUserIdOrderByStartDesc(Long userId);

    List<Booking> findAllByItemIdOrderByStartAsc(Long itemId);

    List<Booking> findAllByItemIdInOrderByStartAsc(List<Long> itemIdList);

    List<Booking> findAllByUserIdAndItemId(Long userId, Long itemId);
}