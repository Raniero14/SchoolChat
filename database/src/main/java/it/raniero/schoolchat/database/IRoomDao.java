package it.raniero.schoolchat.database;

import it.raniero.schoolchat.database.mysql.types.ChatRoom;

import java.util.List;
import java.util.Set;

public interface IRoomDao {



    void createTables();

    boolean addUserToRoom(long userId,long roomId);

    Set<Long> getRoomsFromUserId(long userId);

    Set<Long> getMembersFromRoomId(long roomId);

    void removeUserFromRoom(long userId,long roomId);

    void createRoom(String roomName,boolean auth,String password);

    ChatRoom getRoomByName(String roomName);

    boolean deleteRoom(String roomName);

}
