package it.raniero.schoolchat.server.packet.in;

import it.raniero.schoolchat.api.server.packet.IPacket;
import lombok.Getter;

@Getter
public class ClientSessionPacket implements IPacket {

    private String username;

    private String sessionId;


    @Override
    public void decode(String[] data) {
        username = data[0];
        sessionId = data[1];
    }

    @Override
    public String encode() {
        return null;
    }
}
