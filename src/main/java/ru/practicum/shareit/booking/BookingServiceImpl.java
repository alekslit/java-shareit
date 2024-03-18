package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingFromRequest;
import ru.practicum.shareit.exception.ForbiddenOperationException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.exception.ForbiddenOperationException.*;
import static ru.practicum.shareit.exception.NotAvailableException.*;
import static ru.practicum.shareit.exception.NotFoundException.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    /*------Основные методы------*/
    @Override
    public Booking saveBooking(Long userId, BookingFromRequest bookingFromRequest) {
        log.debug("Попытка добавить новый объект Booking.");
        Item item = getItemById(bookingFromRequest.getItemId());
        checkBookerIsItemOwner(item, userId);
        checkItemAvailable(item);
        User user = getUserById(userId);
        Booking booking = bookingRepository.save(BookingMapper.mapToBooking(bookingFromRequest, user, item));

        return booking;
    }

    @Override
    public Booking approveBooking(Long userId, Long bookingId, String approved) {
        log.debug("Попытка изменить статус (подтвердить или отклонить) объекта Booking.");
        Booking booking = getBookingById(bookingId);
        checkBookingApprove(booking);
        checkOwnerItem(userId, booking.getItem().getUser().getId());
        booking.setStatus(approved.equals("true") ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        booking = bookingRepository.save(booking);

        return booking;
    }

    @Override
    public Booking getBookingById(Long bookingId) {
        log.debug("Попытка получить объект Booking по его id.");
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
            log.debug("{}: {}{}.", NotFoundException.class.getSimpleName(), BOOKING_NOT_FOUND_MESSAGE, bookingId);
            return new NotFoundException(BOOKING_NOT_FOUND_MESSAGE + bookingId, BOOKING_NOT_FOUND_ADVICE);
        });

        return booking;
    }

    @Override
    public List<Booking> getAllUserBookings(Long userId, String state, int from, int size) {
        log.debug("Попытка получить список объектов Booking по id их автора.");
        checkStateFilter(state);
        // проверим, существует ли пользователь:
        getUserById(userId);
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Booking> bookingList = bookingRepository
                .findAllByUserIdOrderByStartDesc(userId, pageRequest).getContent();
        bookingList = filterBookingList(bookingList, BookingFilterState.valueOf(state));

        return bookingList;
    }

    @Override
    public List<Booking> getAllOwnerItemsBookings(Long userId, String state, int from, int size) {
        log.debug("Попытка получить список объектов Booking по id владельца предметов.");
        checkStateFilter(state);
        // проверим, существует ли пользователь:
        getUserById(userId);
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Booking> bookingList = bookingRepository
                .findAllByItemUserIdOrderByStartDesc(userId, pageRequest).getContent();
        bookingList = filterBookingList(bookingList, BookingFilterState.valueOf(state));

        return bookingList;
    }

    /*------Вспомогательные методы------*/
    private User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.debug("{}: {}{}.", NotFoundException.class.getSimpleName(), USER_NOT_FOUND_MESSAGE, userId);
            return new NotFoundException(USER_NOT_FOUND_MESSAGE + userId, USER_NOT_FOUND_ADVICE);
        });

        return user;
    }

    private Item getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            log.debug("{}: {}{}.", NotFoundException.class.getSimpleName(), ITEM_NOT_FOUND_MESSAGE, itemId);
            return new NotFoundException(ITEM_NOT_FOUND_MESSAGE + itemId, ITEM_NOT_FOUND_ADVICE);
        });

        return item;
    }

    // метод для проверки владельца предмета:
    private void checkOwnerItem(Long userId, Long itemOwnerId) {
        if (!itemOwnerId.equals(userId)) {
            log.debug("{}: {}{}.", ForbiddenOperationException.class.getSimpleName(),
                    NOT_ACCESS_TO_APPROVE_MESSAGE, userId);
            throw new ForbiddenOperationException(NOT_ACCESS_TO_APPROVE_MESSAGE + userId, NOT_ACCESS_TO_APPROVE_ADVICE);
        }
    }

    private List<Booking> filterBookingList(List<Booking> bookingList, BookingFilterState state) {
        switch (state) {
            case ALL:
                return bookingList;
            case CURRENT:
                return bookingList.stream()
                        .filter(booking -> booking.getStart().isBefore(LocalDateTime.now())
                                && booking.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case PAST:
                return bookingList.stream()
                        .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingList.stream()
                        .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case WAITING:
                return bookingList.stream()
                        .filter(booking -> booking.getStatus().equals(BookingStatus.WAITING))
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingList.stream()
                        .filter(booking -> booking.getStatus().equals(BookingStatus.REJECTED))
                        .collect(Collectors.toList());
        }

        return bookingList;
    }

    private void checkItemAvailable(Item item) {
        if (!item.getAvailable()) {
            log.debug("{}: {}", NotAvailableException.class.getSimpleName(), ITEM_NOT_AVAILABLE_MESSAGE);
            throw new NotAvailableException(ITEM_NOT_AVAILABLE_MESSAGE, ITEM_NOT_AVAILABLE_ADVICE);
        }
    }

    private void checkStateFilter(String state) {
        boolean isCorrectState = Arrays.stream(BookingFilterState.values())
                .anyMatch(filterState -> filterState.name().equals(state));

        if (!isCorrectState) {
            log.debug("{}: {}{}.", NotAvailableException.class.getSimpleName(), NOT_AVAILABLE_STATE_MESSAGE, state);
            throw new NotAvailableException(NOT_AVAILABLE_STATE_MESSAGE + state, NOT_AVAILABLE_STATE_ADVICE);
        }
    }

    private void checkBookingApprove(Booking booking) {
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            log.debug("{}: {}", NotAvailableException.class.getSimpleName(), NOT_AVAILABLE_APPROVE_MESSAGE);
            throw new NotAvailableException(NOT_AVAILABLE_APPROVE_MESSAGE, NOT_AVAILABLE_APPROVE_ADVICE);
        }
    }

    private void checkBookerIsItemOwner(Item item, Long userId) {
        if (item.getUser().getId().equals(userId)) {
            log.debug("{}: {}{}.", ForbiddenOperationException.class.getSimpleName(), BOOKER_IS_OWNER_MESSAGE, userId);
            throw new ForbiddenOperationException(BOOKER_IS_OWNER_MESSAGE + userId, BOOKER_IS_OWNER_ADVICE);
        }
    }
}