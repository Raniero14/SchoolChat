package it.raniero.schoolchat.api.user.manager;

import it.raniero.schoolchat.api.user.IChatUser;
import it.raniero.schoolchat.api.user.action.UserAction;
import it.raniero.schoolchat.api.user.connection.ISocketWrapper;

import java.util.UUID;

public interface IUserManager<T extends IChatUser> {


    void handleAction(UserAction action);

    T createUser(ISocketWrapper wrapper, UUID uuid);

    T getUser(UUID uuid);

    T getUserById(long userId);

    void removeUser(UUID uuid);

}
