package it.raniero.schoolchat.server.packet.in;

import it.raniero.schoolchat.api.server.packet.IPacket;
import lombok.Getter;

@Getter
public class ClientJoinRoomPacket implements IPacket {

    private long roomId;

    private boolean auth;

    private String password;


    @Override
    public void decode(String[] data) {

        roomId = Long.parseLong(data[0]);
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
