package it.raniero.schoolchat.server.packet.in;

import it.raniero.schoolchat.api.server.packet.IPacket;
import lombok.Getter;

@Getter
public class ClientLoginPacket  implements IPacket {


    private String username;
    private String password;


    @Override
    public void decode(String[] data) {
        username = data[0];
        password = data[1];
    }

    @Override
    public String encode() {
        return "";
    }
}
