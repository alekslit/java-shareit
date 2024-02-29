package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByUserId(Long userId);

    @Query("SELECT i " +
           "FROM Item AS i " +
           "WHERE LOWER(i.name) LIKE CONCAT('%', LOWER(?1), '%') " +
              "OR LOWER(i.description) LIKE CONCAT('%', LOWER(?1), '%') " +
             "AND i.available = true")
    List<Item> searchItemsByNameOrDescription(String text);
}