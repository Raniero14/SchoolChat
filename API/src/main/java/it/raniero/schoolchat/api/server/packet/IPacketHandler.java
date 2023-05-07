package it.raniero.schoolchat.api.server.packet;

import it.raniero.schoolchat.api.user.connection.ISocketWrapper;

public interface IPacketHandler {


    void handle(ISocketWrapper wrapper, IPacket packet);

}
