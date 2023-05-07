package it.raniero.schoolchat.api.user.connection;

import it.raniero.schoolchat.api.server.packet.IPacket;

import java.util.UUID;

public interface ISocketWrapper {


    void listen();

    void disconnect(String message);

    void sendPacket(IPacket packet);


    UUID getPairedUser();



}
