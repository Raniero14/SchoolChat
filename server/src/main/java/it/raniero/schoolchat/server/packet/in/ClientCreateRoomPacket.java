package it.raniero.schoolchat.server.packet.in;

import it.raniero.schoolchat.api.server.packet.IPacket;
import lombok.Getter;

@Getter
public class ClientCreateRoomPacket implements IPacket {



    private String roomName;

    private boolean auth;

    private String password;


    @Override
    public void decode(String[] data) {

        roomName = data[0];
        auth = Boolean.parseBoolean(data[1]);

        if (auth) {
            password = data[2];
        }

    }

    @Override
    public String encode() {
        return null;
    }
}
