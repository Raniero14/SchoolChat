package it.raniero.schoolchat.api.user;

import java.util.UUID;

public interface IChatUser {


    UUID getUniqueId();


    void banUser(String reason);

    boolean hasOpenMessages();


}
