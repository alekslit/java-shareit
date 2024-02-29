package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class CommentMapper {
    public static Comment mapToComment(CommentDto commentDto, User user, Item item) {
        Comment comment = Comment.builder()
                .text(commentDto.getText())
                .user(user)
                .item(item)
                .creationDate(LocalDateTime.now())
                .build();

        return comment;
    }

    public static CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getUser().getName())
                .created(comment.getCreationDate())
                .build();

        return commentDto;
    }

    public static List<CommentDto> mapToCommentDto(List<Comment> commentList) {
        List<CommentDto> commentDtoList = commentList.stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());

        return commentDtoList;
    }
}