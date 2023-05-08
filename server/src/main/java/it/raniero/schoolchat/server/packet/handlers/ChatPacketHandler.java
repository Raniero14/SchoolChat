package it.raniero.schoolchat.server.packet.handlers;

import it.raniero.schoolchat.SchoolChat;
import it.raniero.schoolchat.api.server.packet.IPacket;
import it.raniero.schoolchat.api.server.packet.IPacketHandler;
import it.raniero.schoolchat.api.user.action.UserAction;
import it.raniero.schoolchat.api.user.connection.ISocketWrapper;
import it.raniero.schoolchat.server.packet.in.ClientChatMessagePacket;
import it.raniero.schoolchat.server.packet.in.ClientJoinRoomPacket;
import it.raniero.schoolchat.user.ChatUser;

import java.util.UUID;

public class ChatPacketHandler implements IPacketHandler {



    @Override
    public void handle(ISocketWrapper wrapper, IPacket packet) {

        SchoolChat instance = SchoolChat.getInstance();

        if(packet instanceof ClientChatMessagePacket chatPacket) {

            if(wrapper.getPairedUser() != null) {

                ChatUser user = instance.getUserManager().getUser(wrapper.getPairedUser());

                UserAction action = new UserAction(UserAction.ActionType.MESSAGE,user,chatPacket);

                instance.submitAction(action);

            }

        } else if(packet instanceof ClientJoinRoomPacket joinPacket) {

            if(wrapper.getPairedUser() != null) {

                ChatUser user = instance.getUserManager().getUser(wrapper.getPairedUser());

                UserAction action = new UserAction(UserAction.ActionType.JOIN_ROOM,user,joinPacket);

                instance.submitAction(action);

            }

        }

    }

}
