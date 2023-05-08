package it.raniero.schoolchat.user.manager;

import it.raniero.schoolchat.SchoolChat;
import it.raniero.schoolchat.api.user.IChatUser;
import it.raniero.schoolchat.api.user.action.UserAction;
import it.raniero.schoolchat.api.user.connection.ISocketWrapper;
import it.raniero.schoolchat.api.user.manager.IUserManager;
import it.raniero.schoolchat.database.mysql.types.UserInformation;
import it.raniero.schoolchat.server.packet.in.ClientNicknameChangePacket;
import it.raniero.schoolchat.server.socket.ClientSocketWrapper;
import it.raniero.schoolchat.user.ChatUser;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatUserManager implements IUserManager<ChatUser> {


    private final Map<UUID,ChatUser> users = new ConcurrentHashMap<>();


    @Override
    public void handleAction(UserAction action) {
        SchoolChat instance  = SchoolChat.getInstance();
        switch (action.getType()) {
            case CHANGE_NICKNAME -> {

                ClientNicknameChangePacket packet = (ClientNicknameChangePacket) action.getPacket();

                ChatUser user = (ChatUser) action.getUser();
                boolean result = instance.getChatUserDao().changeUsername(user.getUuid(),packet.getNickname());


            }

        }
    }

    @Override
    public ChatUser createUser(ISocketWrapper wrapper, UUID uuid) {

        SchoolChat instance = SchoolChat.getInstance();

        UserInformation information = instance.getChatUserDao().getUser(uuid);

        if(information == null) {

            return null;

        }

        ChatUser user = new ChatUser(uuid, (ClientSocketWrapper) wrapper);
        user.setInformation(information);

        users.put(uuid,user);

        return user;
    }

    @Override
    public ChatUser getUser(UUID uuid) {
        return users.get(uuid);
    }

    @Override
    public void removeUser(UUID uuid) {

        users.remove(uuid);

    }
}
