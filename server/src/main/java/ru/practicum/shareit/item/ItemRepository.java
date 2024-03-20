package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByUserId(Long userId, Pageable pageable);

    @Query("SELECT i " +
            "FROM Item AS i " +
            "WHERE LOWER(i.name) LIKE CONCAT('%', LOWER(?1), '%') " +
            "OR LOWER(i.description) LIKE CONCAT('%', LOWER(?1), '%') " +
            "AND i.available = true")
    Page<Item> searchItemsByNameOrDescription(String text, Pageable pageable);

    List<Item> findAllByItemRequestIdIn(List<Long> itemRequestIdList);

    List<Item> findAllByItemRequestId(Long itemRequestId);
}