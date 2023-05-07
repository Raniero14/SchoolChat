package it.raniero.schoolchat.api.chat.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ChatRoom {


    private final long roomId;

    private List<Long> users;

    private RoomType type;

    enum RoomType {

        OPEN, PASSWORD, CLOSED

    }
}
