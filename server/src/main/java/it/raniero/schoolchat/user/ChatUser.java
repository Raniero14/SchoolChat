package it.raniero.schoolchat.user;

import it.raniero.schoolchat.api.user.IChatUser;
import it.raniero.schoolchat.database.mysql.types.UserInformation;
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

    private UserInformation information;


    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public void banUser(String reason) {

    }

    @Override
    public boolean hasOpenMessages() {
        return information.isAdmin();
    }
}
