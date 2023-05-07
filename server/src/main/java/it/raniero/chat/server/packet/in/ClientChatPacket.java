package it.raniero.chat.server.packet.in;

import it.raniero.chat.api.server.packet.IPacket;

public class ClientChatPacket implements IPacket {


    private String message;
    private long chatId;

    @Override
    public void decode(String[] data) {
        message = data[0];
        chatId = Long.parseLong(data[1]);
    }

    @Override
    public String encode() {
        return null;
    }
}
