package it.raniero.schoolchat.database.mysql;

import it.raniero.schoolchat.database.IMessageDao;
import it.raniero.schoolchat.database.connection.IConnection;
import it.raniero.schoolchat.database.mysql.types.Message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ChatMessageDao implements IMessageDao {

    private static final String CREATE_TABLE;

    private static final String INSERT_MESSAGE;

    private static final String SELECT_MESSAGES_CHAT;

    private static final String SELECT_MESSAGES_SENDER;

    private static final String SELECT_MESSAGES;



    private final IConnection connection;

    public ChatMessageDao(IConnection connection) {
        this.connection = connection;
        createTables();
    }


    @Override
    public void createTables() {
        try(PreparedStatement tableStmt = connection.getConnection().prepareStatement(CREATE_TABLE)) {

            tableStmt.executeUpdate();

        } catch (SQLException e) {

            System.err.println("Errore nella creazione delle tabelle: ");
            e.printStackTrace();

        }
    }

    @Override
    public Set<Message> getMessagesFromChatId(long chatId) {
        try(PreparedStatement statement = connection.getConnection().prepareStatement(SELECT_MESSAGES_CHAT)) {

            statement.setLong(1,chatId);

            ResultSet resultSet = statement.executeQuery();

            HashSet<Message> messages = new HashSet<>();

            while (resultSet.next()) {
                Message.MessageType type = resultSet.getInt("message_type") == 1 ? Message.MessageType.PRIVATE_MESSAGE : Message.MessageType.ROOM;
                messages.add(new Message(type,
                        resultSet.getLong("chat_id"),
                        resultSet.getLong("sender_id"),
                        resultSet.getString("content"),
                        resultSet.getLong("timestamp")));
            }

            return messages;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    @Override
    public Set<Message> getMessagesFromSenderId(long senderId) {
        try(PreparedStatement statement = connection.getConnection().prepareStatement(SELECT_MESSAGES_SENDER)) {

            statement.setLong(1,senderId);

            ResultSet resultSet = statement.executeQuery();

            HashSet<Message> messages = new HashSet<>();

            while (resultSet.next()) {
                Message.MessageType type = resultSet.getInt("message_type") == 1 ? Message.MessageType.PRIVATE_MESSAGE : Message.MessageType.ROOM;
                messages.add(new Message(type,
                        resultSet.getLong("chat_id"),
                        resultSet.getLong("sender_id"),
                        resultSet.getString("content"),
                        resultSet.getLong("timestamp")));
            }

            return messages;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    @Override
    public void createMessage(Message.MessageType type, long senderId, long chatId, String text) {
        try(PreparedStatement statement = connection.getConnection().prepareStatement(INSERT_MESSAGE)) {

            int mes = type == Message.MessageType.PRIVATE_MESSAGE ? 1 : 0;

            statement.setInt(1,mes);
            statement.setLong(2,senderId);
            statement.setLong(3,chatId);
            statement.setString(4,text);
            statement.setLong(5,System.currentTimeMillis());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public Set<Message> getMessages(long senderId, long chatId) {
        try(PreparedStatement statement = connection.getConnection().prepareStatement(SELECT_MESSAGES)) {

            statement.setLong(1,senderId);
            statement.setLong(2,chatId);

            ResultSet resultSet = statement.executeQuery();

            HashSet<Message> messages = new HashSet<>();

            while (resultSet.next()) {
                Message.MessageType type = resultSet.getInt("message_type") == 1 ? Message.MessageType.PRIVATE_MESSAGE : Message.MessageType.ROOM;
                messages.add(new Message(type,
                        resultSet.getLong("chat_id"),
                        resultSet.getLong("sender_id"),
                        resultSet.getString("content"),
                        resultSet.getLong("timestamp")));
            }

            return messages;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    static {
        CREATE_TABLE = "CREATE TABLE IF NOT EXISTS message (message_id BIGINT AUTO_INCREMENT,message_type TINYINT,sender_id BIGINT NOT NULL, chat_id BIGINT NOT NULL, content TEXT, timestamp BIGINT,PRIMARY KEY(message_id))";
        INSERT_MESSAGE = "INSERT INTO message (message_type,sender_id,chat_id,content,timestamp) VALUES (?,?,?,?,?)";
        SELECT_MESSAGES_CHAT = "SELECT * FROM message WHERE chat_id = ?";
        SELECT_MESSAGES_SENDER = "SELECT * FROM message WHERE sender_id = ?";
        SELECT_MESSAGES = "SELECT * FROM message WHERE sender_id = ? AND chat_id = ?";
    }

}
