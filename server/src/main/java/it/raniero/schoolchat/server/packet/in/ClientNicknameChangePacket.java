package it.raniero.schoolchat.server.packet.in;

import it.raniero.schoolchat.api.server.packet.IPacket;
import lombok.Getter;

@Getter
public class ClientNicknameChangePacket implements IPacket {

    private String nickname;

    @Override
    public void decode(String[] data) {
        nickname = data[0];
    }



    @Override
    public String encode() {
        return null;
    }
}
