package it.raniero.schoolchat.api.server;

import it.raniero.schoolchat.api.server.packet.IPacket;
import it.raniero.schoolchat.api.user.IChatUser;
import it.raniero.schoolchat.api.user.connection.ISocketWrapper;

public interface IChatServer {


    void createServer(String network,int port);

    void shutdownServer(String network,int port);

    void broadcastPacket(IPacket packet);

    void sendPacket(IChatUser user, IPacket packet);


    void handleMessage(ISocketWrapper wrapper, String message);

}
