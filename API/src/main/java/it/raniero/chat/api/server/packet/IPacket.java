package it.raniero.chat.api.server.packet;

public interface IPacket {


    void decode(String[] data);

    String encode();

}
