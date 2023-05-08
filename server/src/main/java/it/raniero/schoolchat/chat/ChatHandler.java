package it.raniero.schoolchat.chat;

import it.raniero.schoolchat.SchoolChat;
import it.raniero.schoolchat.api.chat.IChatHandler;
import it.raniero.schoolchat.api.user.IChatUser;
import it.raniero.schoolchat.api.user.action.UserAction;
import it.raniero.schoolchat.database.mysql.types.ChatRoom;
import it.raniero.schoolchat.server.packet.in.ClientChatMessagePacket;
import it.raniero.schoolchat.server.packet.in.ClientCreateRoomPacket;
import it.raniero.schoolchat.server.packet.in.ClientJoinRoomPacket;
import it.raniero.schoolchat.server.packet.out.ServerGeneralResponsePacket;
import it.raniero.schoolchat.user.ChatUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChatHandler implements IChatHandler {


    private final Map<Long,ChatRoom> rooms = new HashMap<>();

    @Override
    public void init() {

    }

    @Override
    public void createChatRoomMessage(IChatUser user, long roomId, String message) {


    }

    @Override
    public void createPrivateMessage(IChatUser user, long userId, String message) {

    }

    @Override
    public void addToRoom(IChatUser user, long roomId,String password) {

    }

    @Override
    public void kickFromRoom(IChatUser user, long roomId, String reason) {

    }

    @Override
    public void handleAction(UserAction action) {

        SchoolChat instance = SchoolChat.getInstance();

        switch (action.getType()) {

            case MESSAGE -> {

                ClientChatMessagePacket chatPacket = (ClientChatMessagePacket) action.getPacket();
                if(chatPacket.isRoom()) {

                    createChatRoomMessage(action.getUser(),chatPacket.getChatId(),chatPacket.getMessage());

                } else {

                    createPrivateMessage(action.getUser(),chatPacket.getChatId(),chatPacket.getMessage());

                }


            }

            case KICK_USER_FROM_ROOM -> {



            }

            case CREATE_ROOM -> {
                ClientCreateRoomPacket packet = (ClientCreateRoomPacket) action.getPacket();

                boolean response = instance.getChatRoomDao()
                        .createRoom(packet.getRoomName(), packet.isAuth(), packet.getPassword());

                ChatUser user = (ChatUser) action.getUser();

                if(response) {

                    instance.getChatRoomDao().addUserToRoom(user.getInformation().getUserId(),packet.getRoomName());

                    action.getUser().getConnection().sendPacket(
                            new ServerGeneralResponsePacket(
                                    ServerGeneralResponsePacket.ResponseType.SUCCESS,
                                    "Classe creata con successo"
                            ));
                } else {
                    action.getUser().getConnection().sendPacket(
                            new ServerGeneralResponsePacket(
                                    ServerGeneralResponsePacket.ResponseType.ERROR,
                                    "Errore nella creazione della classe"
                            ));
                }


            }

            case JOIN_ROOM -> {
                ClientJoinRoomPacket packet = (ClientJoinRoomPacket) action.getPacket();

                ChatRoom room = instance.getChatRoomDao().getRoom(packet.getRoomId());


                ChatUser user = (ChatUser) action.getUser();

                if(room != null) {

                    if(room.isAuth()) {

                        if(room.getPassword().equals(packet.getPassword())) {
                            instance.getChatRoomDao().addUserToRoom(user.getInformation().getUserId(),packet.getRoomId());
                            action.getUser().getConnection().sendPacket(
                                    new ServerGeneralResponsePacket(
                                            ServerGeneralResponsePacket.ResponseType.SUCCESS,
                                            "Sei entrato nella stanza: " + room.getName()
                                    ));
                        }

                    } else {
                        instance.getChatRoomDao().addUserToRoom(user.getInformation().getUserId(),packet.getRoomId());
                    }


                } else {
                    action.getUser().getConnection().sendPacket(
                            new ServerGeneralResponsePacket(
                                    ServerGeneralResponsePacket.ResponseType.ERROR,
                                    "Errore nella creazione della classe"
                            ));
                }


            }

            case CHANGE_ROOM_NAME -> {

            }

        }
    }


}
