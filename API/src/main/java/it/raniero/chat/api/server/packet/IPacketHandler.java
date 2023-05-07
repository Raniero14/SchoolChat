package it.raniero.chat.api.server.packet;

import it.raniero.chat.api.user.connection.ISocketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IPacketHandler {


    void handle(ISocketWrapper wrapper, IPacket packet);

}
