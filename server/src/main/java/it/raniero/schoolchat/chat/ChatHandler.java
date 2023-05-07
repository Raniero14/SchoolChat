package it.raniero.schoolchat.chat;

import it.raniero.schoolchat.api.chat.IChatHandler;
import it.raniero.schoolchat.api.chat.types.ChatRoom;
import it.raniero.schoolchat.api.user.IChatUser;
import it.raniero.schoolchat.api.user.action.UserAction;
import it.raniero.schoolchat.server.packet.in.ClientChatMessagePacket;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChatHandler implements IChatHandler {


    private final Map<Long,ChatRoom> rooms = new HashMap<>();

    @Override
    public void init() {

    }

    @Override
    public void createChatRoomMessage(IChatUser user, long roomId, String message) {

    }

    @Override
    public void createPrivateMessage(IChatUser user, long userId, String message) {

    }

    @Override
    public void addToRoom(IChatUser user, long roomId,String password) {

    }

    @Override
    public void kickFromRoom(IChatUser user, long roomId, String reason) {

    }

    @Override
    public Optional<ChatRoom> fetchRoom(long roomId) {
        return Optional.empty();
    }

    @Override
    public void handleAction(UserAction action) {
        switch (action.getType()) {

            case MESSAGE -> {

                ClientChatMessagePacket chatPacket = (ClientChatMessagePacket) action.getPacket();
                if(chatPacket.isRoom()) {

                    createChatRoomMessage(action.getUser(),chatPacket.getChatId(),chatPacket.getMessage());

                } else {

                    createPrivateMessage(action.getUser(),chatPacket.getChatId(),chatPacket.getMessage());

                }


            }

            case KICK_USER_FROM_ROOM -> {



            }

            case JOIN_ROOM -> {


            }

            case CHANGE_ROOM_NAME -> {

            }

        }
    }


}
