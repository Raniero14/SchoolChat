package it.raniero.chat.server.packet.in;

import it.raniero.chat.api.server.packet.IPacket;

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
