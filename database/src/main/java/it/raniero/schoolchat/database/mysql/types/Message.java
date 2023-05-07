package it.raniero.schoolchat.database.mysql.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Message {

    private final MessageType type;

    private final long chatId;

    private final long senderId;

    private final String text;

    private final long timestamp;

    public enum MessageType {
        ROOM,PRIVATE_MESSAGE;
    }

}
