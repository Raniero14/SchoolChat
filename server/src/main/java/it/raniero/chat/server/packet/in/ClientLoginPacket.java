package it.raniero.chat.server.packet.in;

import it.raniero.chat.api.server.packet.IPacket;

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
