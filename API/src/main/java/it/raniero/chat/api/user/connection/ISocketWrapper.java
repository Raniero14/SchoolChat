package it.raniero.chat.api.user.connection;

import it.raniero.chat.api.server.packet.IPacket;

import java.util.UUID;

public interface ISocketWrapper {


    void listen();

    void sendPacket(IPacket packet);


    UUID getPairedUser();



}
