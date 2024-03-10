package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class UserMapper {
    // метод для преобразования UserDto в User:
    public static User mapToUser(UserDto userDto) {
        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();

        return user;
    }

    // метод для преобразования User в UserDto:
    public static UserDto mapToUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        return userDto;
    }

    // метод для преобразования списка User в список UserDto:
    public static List<UserDto> mapToUserDto(List<User> users) {
        List<UserDto> userDtoList = users.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());

        return userDtoList;
    }
}