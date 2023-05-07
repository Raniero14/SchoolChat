package it.raniero.schoolchat.server.packet.in;

import it.raniero.schoolchat.api.server.packet.IPacket;
import lombok.Getter;

@Getter
public class ClientChatMessagePacket implements IPacket {

    private boolean room;
    private String message;
    private long chatId;

    @Override
    public void decode(String[] data) {
        room = Boolean.parseBoolean(data[0]);
        chatId = Long.parseLong(data[1]);
        message = data[2];

    }



    @Override
    public String encode() {
        return null;
    }
}
