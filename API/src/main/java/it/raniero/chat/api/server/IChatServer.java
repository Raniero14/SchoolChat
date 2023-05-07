package it.raniero.chat.api.server;

import it.raniero.chat.api.server.packet.IPacket;
import it.raniero.chat.api.user.IChatUser;
import it.raniero.chat.api.user.connection.ISocketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IChatServer {


    void createServer(String network,int port);

    void shutdownServer(String network,int port);

    void broadcastPacket(IPacket packet);

    void sendPacket(IChatUser user, IPacket packet);


    void handleMessage(ISocketWrapper wrapper, String message);

}
