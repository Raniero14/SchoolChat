package it.raniero.schoolchat.server.packet.out;

import it.raniero.schoolchat.api.server.packet.IPacket;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServerGeneralResponsePacket implements IPacket {

    private ServerGeneralResponsePacket.ResponseType response;

    private String message;

    @Override
    public void decode(String[] data) {}

    @Override
    public String encode() {
        return response.toString() + ";" + message;
    }


    public enum ResponseType {
        SUCCESS,ERROR;
    }
}
