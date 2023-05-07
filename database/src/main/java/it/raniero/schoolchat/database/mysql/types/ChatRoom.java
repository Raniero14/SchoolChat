package it.raniero.schoolchat.database.mysql.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
public class ChatRoom {

    private final long roomId;

    private boolean auth;

    private String password;

}
