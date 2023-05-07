package it.raniero.schoolchat.api.user.action;

import it.raniero.schoolchat.api.server.IChatServer;
import it.raniero.schoolchat.api.server.packet.IPacket;
import it.raniero.schoolchat.api.user.IChatUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAction {

    private final ActionType type;

    private final IChatUser user;

    private final IPacket packet;

    public enum ActionType {
        MESSAGE,
        JOIN_ROOM,
        CREATE_ROOM,
        KICK_USER_FROM_ROOM,
        CHANGE_NICKNAME,
        CHANGE_ROOM_NAME

    }
}
