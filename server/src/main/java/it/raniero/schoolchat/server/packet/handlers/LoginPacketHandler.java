package it.raniero.schoolchat.server.packet.handlers;

import it.raniero.schoolchat.SchoolChat;
import it.raniero.schoolchat.api.server.packet.IPacket;
import it.raniero.schoolchat.api.server.packet.IPacketHandler;
import it.raniero.schoolchat.api.user.connection.ISocketWrapper;
import it.raniero.schoolchat.database.connection.utils.LoginStatus;
import it.raniero.schoolchat.server.packet.in.ClientLoginPacket;
import it.raniero.schoolchat.server.packet.in.ClientRegisterPacket;
import it.raniero.schoolchat.server.packet.in.ClientSessionPacket;
import it.raniero.schoolchat.server.packet.out.ServerLoginResponsePacket;
import it.raniero.schoolchat.user.ChatUser;

import java.util.UUID;

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

            ChatUser user = instance.getUserManager().createUser(wrapper,status.getUuid());

            wrapper.pairUser(user.getUuid());
            wrapper.sendPacket(new ServerLoginResponsePacket(ServerLoginResponsePacket.ResponseType.SUCCESS, status.getSessionToken()));

        } else if (packet instanceof ClientSessionPacket sessionPacket) {

            LoginStatus status = instance.getChatUserDao()
                    .loginUserFromSession(sessionPacket.getUsername(), sessionPacket.getSessionId());

            if(status == null) {
                wrapper.sendPacket(new ServerLoginResponsePacket(ServerLoginResponsePacket.ResponseType.FAILED,"none"));
                return;
            }

            instance.getUserManager().createUser(wrapper,status.getUuid());

            wrapper.pairUser(status.getUuid());
            wrapper.sendPacket(new ServerLoginResponsePacket(ServerLoginResponsePacket.ResponseType.SUCCESS, status.getSessionToken()));


        } else if (packet instanceof ClientRegisterPacket registerPacket) {

            UUID uuid = instance.getChatUserDao().registerUser(
                    registerPacket.getUsername(),
                    registerPacket.getPassword());

            if(uuid == null) {
                wrapper.sendPacket(new ServerLoginResponsePacket(ServerLoginResponsePacket.ResponseType.FAILED,"none"));
                return;
            }

            instance.getUserManager().createUser(wrapper,uuid);

            wrapper.pairUser(uuid);
            wrapper.sendPacket(new ServerLoginResponsePacket(ServerLoginResponsePacket.ResponseType.SUCCESS, "none"));

        }


    }
}
