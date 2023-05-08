package it.raniero.schoolchat.server.packet.out;

import it.raniero.schoolchat.api.server.packet.IPacket;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServerChatMessagePacket implements IPacket {

    private boolean room;

    private String senderName;

    private long chatId;

    private String message;

    private long timestamp;

    @Override
    public void decode(String[] data) {}

    @Override
    public String encode() {
        return room + ";" + chatId + ";" + senderName + ";" +  timestamp + ";" + message;
    }


    public enum ResponseType {
        SUCCESS,FAILED;
    }
}
