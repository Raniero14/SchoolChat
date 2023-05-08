package it.raniero.schoolchat.server.packet.out;

import it.raniero.schoolchat.api.server.packet.IPacket;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class ServerLoginResponsePacket implements IPacket {


    private ResponseType response;

    private String sessionToken;



    @Override
    public void decode(String[] data) {}

    @Override
    public String encode() {
        return response.toString() + ";" + sessionToken;
    }


    public enum ResponseType {
        SUCCESS,FAILED;
    }
}
