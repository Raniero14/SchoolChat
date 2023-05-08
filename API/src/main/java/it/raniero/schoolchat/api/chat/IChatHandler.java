package it.raniero.schoolchat.api.chat;

import it.raniero.schoolchat.api.user.IChatUser;
import it.raniero.schoolchat.api.user.action.UserAction;

import java.util.Optional;

public interface IChatHandler {


    void init();
    void createChatRoomMessage(IChatUser user, long roomId, String message);

    void createPrivateMessage(IChatUser user, long userId,String message);

    void addToRoom(IChatUser user, long roomId,String password);

    void kickFromRoom(IChatUser user, long roomId,String reason);

    void handleAction(UserAction action);


}

