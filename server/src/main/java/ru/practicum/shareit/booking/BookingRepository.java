package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByUserIdOrderByStartDesc(Long userId, Pageable pageable);

    Page<Booking> findAllByItemUserIdOrderByStartDesc(Long userId, Pageable pageable);

    List<Booking> findAllByItemIdOrderByStartAsc(Long itemId);

    List<Booking> findAllByItemIdInOrderByStartAsc(List<Long> itemIdList);

    List<Booking> findAllByUserIdAndItemId(Long userId, Long itemId);
}