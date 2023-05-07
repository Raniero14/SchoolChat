package it.raniero.schoolchat.api.user.manager;

import it.raniero.schoolchat.api.user.IChatUser;
import it.raniero.schoolchat.api.user.action.UserAction;

import java.util.UUID;

public interface IUserManager<T extends IChatUser> {


    void handleAction(UserAction action);

    void addUser(IChatUser user);

    T getUser(UUID uuid);

    void removeUser(UUID uuid);

}
