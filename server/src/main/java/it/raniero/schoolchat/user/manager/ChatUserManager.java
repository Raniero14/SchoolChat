package it.raniero.schoolchat.user.manager;

import it.raniero.schoolchat.SchoolChat;
import it.raniero.schoolchat.api.user.IChatUser;
import it.raniero.schoolchat.api.user.action.UserAction;
import it.raniero.schoolchat.api.user.manager.IUserManager;
import it.raniero.schoolchat.server.packet.in.ClientNicknameChangePacket;
import it.raniero.schoolchat.user.ChatUser;

import java.util.UUID;

public class ChatUserManager implements IUserManager<ChatUser> {

    @Override
    public void handleAction(UserAction action) {
        SchoolChat api  = SchoolChat.getInstance();
        switch (action.getType()) {
            case CHANGE_NICKNAME -> {

                ClientNicknameChangePacket packet = (ClientNicknameChangePacket) action.getPacket();

                ChatUser user = (ChatUser) action.getUser();
                boolean result = api.getChatUserDao().changeUsername(user.getUuid(),packet.getNickname());

            }
        }
    }

    @Override
    public void addUser(IChatUser user) {

    }

    @Override
    public ChatUser getUser(UUID uuid) {
        return null;
    }

    @Override
    public void removeUser(UUID uuid) {

    }
}
