package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.ForbiddenOperationException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.exception.ForbiddenOperationException.NOT_OWNER_ITEM_ADVICE;
import static ru.practicum.shareit.exception.ForbiddenOperationException.NOT_OWNER_ITEM_MESSAGE;
import static ru.practicum.shareit.exception.NotAvailableException.NOT_AVAILABLE_COMMENT_ADVICE;
import static ru.practicum.shareit.exception.NotAvailableException.NOT_AVAILABLE_COMMENT_MESSAGE;
import static ru.practicum.shareit.exception.NotFoundException.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    /*-------Основные методы (Item)-------*/
    @Override
    public Item saveItem(Long userId, ItemDto itemDto) {
        log.debug("Попытка добавить новый объект Item.");
        User user = getUserById(userId);
        Item item = ItemMapper.mapToItem(itemDto, user);
        // если есть id запроса на предмет:
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = getItemRequestById(itemDto.getRequestId());
            item.setItemRequest(itemRequest);
        }
        item = itemRepository.save(item);

        return item;
    }

    @Override
    public Item updateItem(Long userId, ItemDto itemDto, Long itemId) {
        log.debug("Попытка обновить информацию об объекте Item.");
        Item item = checkOwnerItem(userId, itemId);
        item = itemRepository.save(updateItemObject(item, itemDto));

        return item;
    }

    @Override
    public Item getItemById(Long itemId) {
        log.debug("Попытка получить объект Item по его id.");
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            log.debug("{}: {}{}.", NotFoundException.class.getSimpleName(), ITEM_NOT_FOUND_MESSAGE, itemId);
            return new NotFoundException(ITEM_NOT_FOUND_MESSAGE + itemId, ITEM_NOT_FOUND_ADVICE);
        });

        return item;
    }

    @Override
    public List<Item> findAllItemsByOwnerId(Long userId, int from, int size) {
        log.debug("Попытка получить список объектов Item по id пользователя (владельца).");
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Item> itemList = itemRepository.findAllByUserId(userId, pageRequest).getContent();

        return itemList;
    }

    @Override
    public List<Item> searchItemsByNameOrDescription(String text, int from, int size) {
        log.debug("Попытка найти список объектов Item по их названию и/или описанию.");
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Item> itemList = (text.isBlank() ?
                new ArrayList<>() : itemRepository.searchItemsByNameOrDescription(text, pageRequest).getContent());

        return itemList;
    }

    /*-------Основные методы (Comment)-------*/
    @Override
    public Comment saveComment(Long userId, Long itemId, CommentDto commentDto) {
        log.debug("Попытка добавить новый объект Comment.");
        // проверяем что User и Item существуют:
        User user = getUserById(userId);
        Item item = getItemById(itemId);
        // проверяем, что аренда (Booking) существует:
        List<Booking> bookingList = getBookingsByUserIdAndItemId(userId, itemId);
        // проверяем что аренда состоялась:
        checkBooking(bookingList);
        // сохраняем комментарий:
        Comment comment = commentRepository.save(CommentMapper.mapToComment(commentDto, user, item));

        return comment;
    }

    /*-------Вспомогательные методы (Booking)-------*/
    @Override
    public ItemDto addBookingsDtoToItem(ItemDto itemDto) {
        List<Booking> bookingList = bookingRepository.findAllByItemIdOrderByStartAsc(itemDto.getId()).stream()
                .filter(booking -> !booking.getStatus().equals(BookingStatus.REJECTED))
                .collect(Collectors.toList());
        bookingList = filterLastAndNextBooking(bookingList);

        return addTwoBookings(bookingList, itemDto);
    }

    @Override
    public List<ItemDto> addBookingsDtoToItem(List<ItemDto> itemDtoList) {
        // переменная для сбора итогового списка:
        List<ItemDto> result = new ArrayList<>();
        // получаем id всех Item для поиска их Booking:
        List<Long> itemIdList = itemDtoList.stream()
                .map(ItemDto::getId)
                .collect(Collectors.toList());
        // находим все Booking:
        List<Booking> bookingList = bookingRepository.findAllByItemIdInOrderByStartAsc(itemIdList);
        // проходим по списку Booking id-шниками Item:
        for (ItemDto item : itemDtoList) {
            List<Booking> itemBookings = bookingList.stream()
                    .filter(booking -> booking.getItem().getId().equals(item.getId())
                            && !booking.getStatus().equals(BookingStatus.REJECTED))
                    .collect(Collectors.toList());
            itemBookings = filterLastAndNextBooking(itemBookings);
            // добавляем к Item:
            ItemDto itemDto = addTwoBookings(itemBookings, item);
            // сохраняем в список:
            result.add(itemDto);
        }

        return result;
    }

    /*-------Вспомогательные методы (Comment)-------*/
    @Override
    public ItemDto addCommentToItem(ItemDto itemDto) {
        List<Comment> commentList = commentRepository.findAllByItemId(itemDto.getId());
        itemDto.setComments(CommentMapper.mapToCommentDto(commentList));

        return itemDto;
    }

    @Override
    public List<ItemDto> addCommentToItem(List<ItemDto> itemDtoList) {
        // переменная для сбора итогового списка:
        List<ItemDto> result = new ArrayList<>();
        // получаем id всех Item для поиска их Comment:
        List<Long> itemIdList = itemDtoList.stream()
                .map(ItemDto::getId)
                .collect(Collectors.toList());
        // находим все Comment:
        List<Comment> commentList = commentRepository.findAllByItemIdIn(itemIdList);
        // проходим по списку Comment id-шниками Item, и находим все комментарии к предмету:
        for (ItemDto item : itemDtoList) {
            List<Comment> commentByItem = commentList.stream()
                    .filter(comment -> comment.getItem().getId().equals(item.getId()))
                    .collect(Collectors.toList());
            // добавляем к Item:
            item.setComments(CommentMapper.mapToCommentDto(commentByItem));
            // сохраняем в список:
            result.add(item);
        }

        return result;
    }

    /*-------Вспомогательные методы (Остальные)-------*/
    private Item checkOwnerItem(Long userId, Long itemId) {
        Item item = getItemById(itemId);
        if (!item.getUser().getId().equals(userId)) {
            log.debug("{}: {}{}.", ForbiddenOperationException.class.getSimpleName(), NOT_OWNER_ITEM_MESSAGE, userId);
            throw new ForbiddenOperationException(NOT_OWNER_ITEM_MESSAGE + userId, NOT_OWNER_ITEM_ADVICE);
        }

        return item;
    }

    private Item updateItemObject(Item item, ItemDto itemDto) {
        // обновляем данные, если они инициализированы:
        item.setName(itemDto.getName() != null ? itemDto.getName() : item.getName());
        item.setDescription(itemDto.getDescription() != null ? itemDto.getDescription() : item.getDescription());
        item.setAvailable(itemDto.getAvailable() != null ? itemDto.getAvailable() : item.getAvailable());

        return item;
    }

    private User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.debug("{}: {}{}.", NotFoundException.class.getSimpleName(), USER_NOT_FOUND_MESSAGE, userId);
            return new NotFoundException(USER_NOT_FOUND_MESSAGE + userId, USER_NOT_FOUND_ADVICE);
        });

        return user;
    }

    private ItemDto addTwoBookings(List<Booking> bookingList, ItemDto itemDto) {
        switch (bookingList.size()) {
            case 0:
                return itemDto;
            case 1:
                itemDto.setLastBooking(BookingMapper.mapToBookingForItemDto(bookingList.get(0)));
                break;
            case 2:
                itemDto.setLastBooking(BookingMapper.mapToBookingForItemDto(bookingList.get(0)));
                itemDto.setNextBooking(BookingMapper.mapToBookingForItemDto(bookingList.get(1)));
                break;
        }

        return itemDto;
    }

    private List<Booking> getBookingsByUserIdAndItemId(Long userId, Long itemId) {
        List<Booking> bookingList = bookingRepository.findAllByUserIdAndItemId(userId, itemId);
        if (bookingList.size() == 0) {
            log.debug("{}: {} = {}, {} = {} {}.", NotFoundException.class.getSimpleName(),
                    "Пользователь с id", userId, "не брал предмет с id", itemId, "в аренду.");
            throw new NotFoundException(String.format("Пользователь с id = %d, не брал предмет с id = %d в аренду.",
                    userId, itemId), BOOKING_NOT_FOUND_ADVICE);
        }

        return bookingList;
    }

    private List<Booking> filterLastAndNextBooking(List<Booking> bookingList) {
        List<Booking> result = new ArrayList<>();
        // отбираем прошедшие аренды:
        List<Booking> pastBookings = bookingList.stream()
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
        // добавляем последнюю прошлую аренду:
        if (pastBookings.size() > 0) {
            result.add(pastBookings.get(pastBookings.size() - 1));
        }

        // отбираем будущие аренды:
        List<Booking> futureBookings = bookingList.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
        // добавляем первую будущую аренду:
        if (futureBookings.size() > 0) {
            result.add(futureBookings.get(0));
        }

        return result;
    }

    private void checkBooking(List<Booking> bookingList) {
        boolean anyBookingApprovedAndStarted = bookingList.stream()
                .anyMatch(booking -> booking.getStatus().equals(BookingStatus.APPROVED)
                        && booking.getStart().isBefore(LocalDateTime.now()));

        if (!anyBookingApprovedAndStarted) {
            log.debug("{}: {}.", NotAvailableException.class.getSimpleName(), NOT_AVAILABLE_COMMENT_MESSAGE);
            throw new NotAvailableException(NOT_AVAILABLE_COMMENT_MESSAGE, NOT_AVAILABLE_COMMENT_ADVICE);
        }
    }

    private ItemRequest getItemRequestById(Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() -> {
            log.debug("{}: {}{}.", NotFoundException.class.getSimpleName(), REQUEST_NOT_FOUND_MESSAGE, requestId);
            return new NotFoundException(REQUEST_NOT_FOUND_MESSAGE + requestId, REQUEST_NOT_FOUND_ADVICE);
        });

        return itemRequest;
    }
}