package it.raniero.schoolchat.chat;

import it.raniero.schoolchat.SchoolChat;
import it.raniero.schoolchat.api.chat.IChatHandler;
import it.raniero.schoolchat.api.user.IChatUser;
import it.raniero.schoolchat.api.user.action.UserAction;
import it.raniero.schoolchat.database.mysql.types.ChatRoom;
import it.raniero.schoolchat.database.mysql.types.Message;
import it.raniero.schoolchat.server.packet.PacketManager;
import it.raniero.schoolchat.server.packet.in.ClientChatMessagePacket;
import it.raniero.schoolchat.server.packet.in.ClientCreateRoomPacket;
import it.raniero.schoolchat.server.packet.in.ClientJoinRoomPacket;
import it.raniero.schoolchat.server.packet.out.ServerChatMessagePacket;
import it.raniero.schoolchat.server.packet.out.ServerGeneralResponsePacket;
import it.raniero.schoolchat.user.ChatUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ChatHandler implements IChatHandler {


    private final Map<Long,ChatRoom> rooms = new HashMap<>();
    private final Map<Long, Set<Long>> members = new HashMap<>();

    @Override
    public void init() {

        SchoolChat instance = SchoolChat.getInstance();

        for (ChatRoom room : instance.getChatRoomDao().getRooms()) {
            rooms.put(room.getRoomId(),room);
            members.put(room.getRoomId(),instance.getChatRoomDao().getMembersFromRoomId(room.getRoomId()));
            System.out.println("aggiunta stanza: " + room.getRoomId());
        }
    }

    @Override
    public void createChatRoomMessage(IChatUser user, long roomId, String message) {


        SchoolChat instance = SchoolChat.getInstance();
        ChatUser chatUser = (ChatUser) user;

        if(members.containsKey(roomId) && members.get(roomId).contains(chatUser.getInformation().getUserId())) {

            instance.getChatMessageDao().createMessage(Message.MessageType.ROOM,chatUser.getInformation().getUserId(),roomId,message);

            ServerChatMessagePacket messagePacket = new ServerChatMessagePacket(true,
                    chatUser.getInformation().getUsername()
                    ,roomId,message,System.currentTimeMillis());

            for (Long userId : members.get(roomId)) {
                ChatUser receiver = instance.getUserManager().getUserById(userId);
                if(receiver != null) {
                    receiver.getConnection().sendPacket(messagePacket);
                }
            }

        }

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
                        .createRoom(packet.getRoomName().toLowerCase(), packet.isAuth(), packet.getPassword());


                ChatUser user = (ChatUser) action.getUser();

                if(response) {

                    instance.getChatRoomDao().addUserToRoom(user.getInformation().getUserId(),packet.getRoomName().toLowerCase());
                    ChatRoom room = instance.getChatRoomDao().getRoomByName(packet.getRoomName().toLowerCase());

                    action.getUser().getConnection().sendPacket(
                            new ServerGeneralResponsePacket(
                                    ServerGeneralResponsePacket.ResponseType.SUCCESS,
                                    "Classe creata con successo"
                            ));
                    rooms.put(room.getRoomId(),room);
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

                ChatRoom room = rooms.get(packet.getRoomId());

                ChatUser user = (ChatUser) action.getUser();

                if(room != null) {

                    if(room.isAuth()) {

                        if(room.getPassword().equals(packet.getPassword())) {
                            boolean added = instance.getChatRoomDao().addUserToRoom(user.getInformation().getUserId(),packet.getRoomId());
                            if(added) {
                                members.get(room.getRoomId()).add(user.getInformation().getUserId());
                                action.getUser().getConnection().sendPacket(
                                        new ServerGeneralResponsePacket(
                                                ServerGeneralResponsePacket.ResponseType.SUCCESS,
                                                "Sei entrato nella stanza: " + room.getName()
                                        ));
                            }

                        }

                    } else {
                        boolean added = instance.getChatRoomDao().addUserToRoom(user.getInformation().getUserId(),packet.getRoomId());
                        if(added) {

                            members.get(room.getRoomId()).add(user.getInformation().getUserId());
                            action.getUser().getConnection().sendPacket(
                                    new ServerGeneralResponsePacket(
                                            ServerGeneralResponsePacket.ResponseType.SUCCESS,
                                            "Sei entrato nella stanza: " + room.getName()
                                    ));
                        }

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
