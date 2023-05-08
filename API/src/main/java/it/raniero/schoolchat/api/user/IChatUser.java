package it.raniero.schoolchat.api.user;

import it.raniero.schoolchat.api.user.connection.ISocketWrapper;

import java.util.UUID;

public interface IChatUser {


    UUID getUniqueId();


    void banUser(String reason);

    ISocketWrapper getConnection();
    boolean hasOpenMessages();


}
