package it.raniero.schoolchat.server.packet.handlers;

import it.raniero.schoolchat.SchoolChat;
import it.raniero.schoolchat.api.server.packet.IPacket;
import it.raniero.schoolchat.api.server.packet.IPacketHandler;
import it.raniero.schoolchat.api.user.action.UserAction;
import it.raniero.schoolchat.api.user.connection.ISocketWrapper;
import it.raniero.schoolchat.server.packet.in.ClientChatMessagePacket;
import it.raniero.schoolchat.server.packet.in.ClientCreateRoomPacket;
import it.raniero.schoolchat.user.ChatUser;

public class ClientSettingsHandler implements IPacketHandler {



    @Override
    public void handle(ISocketWrapper wrapper, IPacket packet) {

        SchoolChat instance = SchoolChat.getInstance();

        if(packet instanceof ClientCreateRoomPacket createRoomPacket) {

            if(wrapper.getPairedUser() != null) {

                ChatUser user = instance.getUserManager().getUser(wrapper.getPairedUser());

                UserAction action = new UserAction(UserAction.ActionType.CREATE_ROOM,user,createRoomPacket);

                instance.submitAction(action);

            }
        }
    }


}
