package it.raniero.schoolchat.api.server.packet;

public interface IPacket {


    void decode(String[] data);

    String encode();

}
