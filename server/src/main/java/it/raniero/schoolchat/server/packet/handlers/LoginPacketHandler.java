package it.raniero.schoolchat.server.packet.handlers;

import it.raniero.schoolchat.SchoolChat;
import it.raniero.schoolchat.api.server.packet.IPacket;
import it.raniero.schoolchat.api.server.packet.IPacketHandler;
import it.raniero.schoolchat.api.user.connection.ISocketWrapper;
import it.raniero.schoolchat.database.connection.utils.LoginStatus;
import it.raniero.schoolchat.server.packet.in.ClientLoginPacket;
import it.raniero.schoolchat.server.packet.in.ClientSessionPacket;
import it.raniero.schoolchat.server.packet.out.ServerLoginResponsePacket;

public class LoginPacketHandler implements IPacketHandler {



    @Override
    public void handle(ISocketWrapper wrapper, IPacket packet) {


        SchoolChat instance = SchoolChat.getInstance();

        if(packet instanceof ClientLoginPacket loginPacket) {

            LoginStatus status = instance.getChatUserDao()
                    .loginUser(loginPacket.getUsername(), loginPacket.getPassword());

            if(status == null) {
                wrapper.sendPacket(new ServerLoginResponsePacket(ServerLoginResponsePacket.ResponseType.FAILED,"none"));
                return;
            }

            instance.getUserManager().createUser(wrapper,status.getUuid());

            wrapper.sendPacket(new ServerLoginResponsePacket(ServerLoginResponsePacket.ResponseType.SUCCESS, status.getSessionToken()));

        } else if (packet instanceof ClientSessionPacket sessionPacket) {

            LoginStatus status = instance.getChatUserDao()
                    .loginUserFromSession(sessionPacket.getUsername(), sessionPacket.getSessionId());

            if(status == null) {
                wrapper.sendPacket(new ServerLoginResponsePacket(ServerLoginResponsePacket.ResponseType.FAILED,"none"));
                return;
            }

            instance.getUserManager().createUser(wrapper,status.getUuid());

            wrapper.sendPacket(new ServerLoginResponsePacket(ServerLoginResponsePacket.ResponseType.SUCCESS, status.getSessionToken()));


        }


    }
}
