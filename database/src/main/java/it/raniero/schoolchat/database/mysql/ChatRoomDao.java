package it.raniero.schoolchat.database.mysql;

import it.raniero.schoolchat.database.IRoomDao;
import it.raniero.schoolchat.database.connection.IConnection;
import it.raniero.schoolchat.database.mysql.types.ChatRoom;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ChatRoomDao implements IRoomDao {


    private static final String CREATE_ROOM_TABLE;

    private static final String CREATE_ROOM_USER_TABLE;

    private static final String INSERT_ROOM;

    private static final String SELECT_ROOM;
    private static final String SELECT_ROOM_NAME;

    private static final String DELETE_ROOM;

    private static final String INSERT_MEMBER;

    private static final String DELETE_MEMBER;

    private static final String SELECT_MEMBERS;

    private static final String SELECT_ROOMS;



    private final IConnection connection;

    public ChatRoomDao(IConnection connection) {
        this.connection = connection;
        createTables();
    }


    @Override
    public void createTables() {
        try(PreparedStatement roomtableStmt = connection.getConnection().prepareStatement(CREATE_ROOM_TABLE);
            PreparedStatement roomUserTableStmt = connection.getConnection().prepareStatement(CREATE_ROOM_USER_TABLE)) {

            roomtableStmt.executeUpdate();
            roomUserTableStmt.executeUpdate();

        } catch (SQLException e) {

            System.err.println("Errore nella creazione delle tabelle: ");
            e.printStackTrace();

        }
    }

    @Override
    public boolean addUserToRoom(long userId, long roomId) {
        try(PreparedStatement statement = connection.getConnection().prepareStatement(INSERT_MEMBER)) {

            statement.setLong(1,roomId);
            statement.setLong(2,userId);

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addUserToRoom(long userId, String roomName) {
        try(PreparedStatement statement = connection.getConnection().prepareStatement(INSERT_MEMBER);
            PreparedStatement selectRoomStatement = connection.getConnection().prepareStatement(SELECT_ROOM_NAME)) {


            selectRoomStatement.setString(1,roomName);

            ResultSet resultSet = selectRoomStatement.executeQuery();

            if(resultSet.next()) {

                statement.setLong(1,resultSet.getLong("room_id"));
                statement.setLong(2,userId);

                return statement.executeUpdate() != 0;

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ChatRoom getRoom(long roomId) {
        try(PreparedStatement statement = connection.getConnection().prepareStatement(SELECT_ROOM)) {

            statement.setLong(1,roomId);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return new ChatRoom(
                        resultSet.getLong("room_id"),
                        resultSet.getString("name"),
                        resultSet.getBoolean("auth"),
                        resultSet.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Set<Long> getRoomsFromUserId(long userId) {
        try(PreparedStatement statement = connection.getConnection().prepareStatement(SELECT_ROOMS)) {

            statement.setLong(1,userId);

            ResultSet resultSet = statement.executeQuery();

            HashSet<Long> rooms = new HashSet<>();

            while (resultSet.next()) {
                rooms.add(resultSet.getLong("room_id"));
            }

            return rooms;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    @Override
    public Set<Long> getMembersFromRoomId(long roomId) {
        try(PreparedStatement statement = connection.getConnection().prepareStatement(SELECT_MEMBERS)) {

            statement.setLong(1,roomId);

            ResultSet resultSet = statement.executeQuery();

            HashSet<Long> rooms = new HashSet<>();

            while (resultSet.next()) {
                rooms.add(resultSet.getLong("user_id"));
            }

            return rooms;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    @Override
    public void removeUserFromRoom(long userId, long roomId) {
        try(PreparedStatement statement = connection.getConnection().prepareStatement(DELETE_MEMBER)) {

            statement.setLong(1,roomId);
            statement.setLong(2,userId);

            statement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean createRoom(String roomName, boolean auth, String password) {
        try(PreparedStatement statement = connection.getConnection().prepareStatement(INSERT_ROOM)) {

            statement.setString(1,roomName);
            statement.setBoolean(2,auth);
            statement.setString(3,password);

            return statement.executeUpdate() != 0;


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ChatRoom getRoomByName(String roomName) {
        try(PreparedStatement statement = connection.getConnection().prepareStatement(SELECT_ROOM_NAME)) {

            statement.setString(1,roomName);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return new ChatRoom(
                        resultSet.getLong("room_id"),
                        resultSet.getString("name"),
                        resultSet.getBoolean("auth"),
                        resultSet.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean deleteRoom(String roomName) {
        try(PreparedStatement statement = connection.getConnection().prepareStatement(DELETE_ROOM)) {

            statement.setString(1,roomName);

            return statement.executeUpdate() != 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    static {

        CREATE_ROOM_TABLE = "CREATE TABLE IF NOT EXISTS room (room_id BIGINT NOT NULL, name VARCHAR(32), auth BOOLEAN, password VARCHAR(64), PRIMARY KEY(room_id), UNIQUE(name))";
        CREATE_ROOM_USER_TABLE = "CREATE TABLE IF NOT EXISTS room_user (room_id BIGINT NOT NULL, user_id BIGINT NOT NULL,FOREIGN KEY(room_id) REFERENCES room(room_id) ON DELETE CASCADE,FOREIGN KEY(user_id) REFERENCES user(user_id) ON DELETE CASCADE)";
        INSERT_ROOM = "INSERT INTO room (name,auth,password) VALUES (?,?,?)";

        DELETE_ROOM = "DELETE FROM room WHERE name = ?";
        DELETE_MEMBER = "DELETE FROM room_user WHERE room_id = ? AND user_id = ?";
        SELECT_ROOM_NAME = "SELECT * FROM room WHERE name = ?";

        SELECT_ROOM = "SELECT * FROM room WHERE roomId = ?";


        INSERT_MEMBER = "INSERT INTO room_user (room_id,user_id) VALUES (?,?)";


        SELECT_MEMBERS = "SELECT user_id FROM room_user WHERE room_id = ?";
        SELECT_ROOMS = "SELECT room_id FROM room_user WHERE user_id = ?";

    }

}
