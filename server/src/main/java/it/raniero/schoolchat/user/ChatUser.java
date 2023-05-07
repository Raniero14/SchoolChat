package it.raniero.schoolchat.user;

import it.raniero.schoolchat.api.user.IChatUser;
import it.raniero.schoolchat.server.socket.ClientSocketWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ChatUser implements IChatUser {


    private final UUID uuid;

    private final ClientSocketWrapper socketWrapper;


    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public void banUser(String reason) {

    }

    @Override
    public boolean hasOpenMessages() {
        return false;
    }
}
