package it.raniero.schoolchat.database.mysql;

import it.raniero.schoolchat.database.IUserDao;
import it.raniero.schoolchat.database.connection.IConnection;
import it.raniero.schoolchat.database.connection.utils.LoginStatus;
import it.raniero.schoolchat.database.mysql.types.UserInformation;
import it.raniero.schoolchat.database.mysql.utils.StringUtils;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.UUID;

public class ChatUserDao implements IUserDao {

    private static final String CREATE_TABLE;

    private static final String INSERT_USER;

    private static final String SELECT_USER_PASSWORD;

    private static final String SELECT_USER_SESSION;

    private static final String SELECT_USER_UUID;

    private static final String SELECT_USER_USERNAME;

    private static final String UPDATE_SESSION;

    private static final String UPDATE_NICKNAME;




    private final IConnection connection;

    public ChatUserDao(IConnection connection) {
        this.connection = connection;
        createTables();
    }



    @Override
    public void createTables() {
        try(Connection conn = connection.getConnection();
            PreparedStatement tableStmt = conn.prepareStatement(CREATE_TABLE)) {

            tableStmt.executeUpdate();

        } catch (SQLException e) {

            System.err.println("Errore nella creazione delle tabelle: ");
            e.printStackTrace();

        }
    }

    @Override
    public UUID registerUser(String username, String password) {

        if(username.length() > 32) {
            return null;
        }

        try(Connection conn = connection.getConnection();
                PreparedStatement insertUser = conn.prepareStatement(INSERT_USER)) {

            String user = "ChatUser:" + username + ":" + System.currentTimeMillis();
            UUID uniqueId = UUID.nameUUIDFromBytes(user.getBytes(StandardCharsets.UTF_8));

            insertUser.setString(1,uniqueId.toString());
            insertUser.setString(2,username);
            insertUser.setString(3, StringUtils.sha256(password));
            insertUser.setBoolean(4,false);
            insertUser.setBoolean(5,false);


            return insertUser.executeUpdate() != 0 ? uniqueId : null;

        } catch (SQLIntegrityConstraintViolationException e) {

            return null;

        }
        catch (SQLException e) {

            System.err.println("Errore nella registrazione utente: ");
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public LoginStatus loginUser(String username, String password) {

        try(Connection conn = connection.getConnection();
            PreparedStatement selectUser = conn.prepareStatement(SELECT_USER_PASSWORD);
            PreparedStatement updateSession = conn.prepareStatement(UPDATE_SESSION)) {


            selectUser.setString(1,username);
            selectUser.setString(2, StringUtils.sha256(password));

            ResultSet resultSet = selectUser.executeQuery();

            if(resultSet.next()) {

                String session = StringUtils.sha256(username  + ":" + password + ":" + System.currentTimeMillis());

                updateSession.setString(1,session);
                updateSession.setString(2,resultSet.getString("unique_id"));
                updateSession.executeUpdate();

                return new LoginStatus(UUID.fromString(resultSet.getString("unique_id")),session);

            } else {

                return null;

            }

        }
        catch (SQLException e) {

            System.err.println("Errore nel login utente: ");
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public LoginStatus loginUserFromSession(String username, String session) {
        try(PreparedStatement selectUser = connection.getConnection().prepareStatement(SELECT_USER_SESSION)) {


            selectUser.setString(1,username);
            selectUser.setString(2, session);

            ResultSet resultSet = selectUser.executeQuery();

            if(resultSet.next()) {

                return new LoginStatus(UUID.fromString(resultSet.getString("unique_id")),session);

            } else {

                return null;

            }

        }
        catch (SQLException e) {

            System.err.println("Errore nel login utente (sessione): ");
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public UserInformation getUser(String username) {
        try(Connection conn = connection.getConnection();
            PreparedStatement selectUser = conn.prepareStatement(SELECT_USER_USERNAME)) {


            selectUser.setString(1,username);

            ResultSet resultSet = selectUser.executeQuery();

            if(resultSet.next()) {

                return new UserInformation(resultSet.getLong("user_id"),
                        UUID.fromString(resultSet.getString("unique_id")),
                        resultSet.getString("username"),
                        resultSet.getBoolean("admin"),
                        resultSet.getBoolean("open_messages"));

            } else {

                return null;

            }

        }
        catch (SQLException e) {

            System.err.println("Errore nel fetch utente: ");
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public UserInformation getUser(UUID uuid) {
        try(Connection conn = connection.getConnection();
            PreparedStatement selectUser = conn.prepareStatement(SELECT_USER_UUID)) {


            selectUser.setString(1,uuid.toString());

            ResultSet resultSet = selectUser.executeQuery();

            if(resultSet.next()) {

                return new UserInformation(resultSet.getLong("user_id"),
                        UUID.fromString(resultSet.getString("unique_id")),
                        resultSet.getString("username"),
                        resultSet.getBoolean("admin"),
                        resultSet.getBoolean("open_messages"));

            } else {

                return null;

            }

        }
        catch (SQLException e) {

            System.err.println("Errore nel fetch utente: ");
            e.printStackTrace();

        }
        return null;
    }


    @Override
    public boolean changeUsername(UUID uuid, String selectedUsername) {

        if(selectedUsername.length() > 32) {
            return false;
        }

        try(Connection conn = connection.getConnection();
            PreparedStatement insertUser = conn.prepareStatement(UPDATE_NICKNAME)) {


            insertUser.setString(1,selectedUsername);
            insertUser.setString(2,uuid.toString());
            insertUser.executeUpdate();

            return true;

        } catch (SQLIntegrityConstraintViolationException e) {

            return false;

        }
        catch (SQLException e) {

            System.err.println("Errore nella registrazione utente: ");
            e.printStackTrace();

            return false;

        }
    }



    static {

        CREATE_TABLE = "CREATE TABLE IF NOT EXISTS user (user_id BIGINT AUTO_INCREMENT,unique_id VARCHAR(128), username VARCHAR(32),password VARCHAR(64),session_id VARCHAR(64),open_messages BOOLEAN,admin BOOLEAN, PRIMARY KEY (user_id), UNIQUE(username),UNIQUE(unique_id))";
        INSERT_USER = "INSERT INTO user (unique_id, username, password, admin, open_messages) VALUES (?,?,?,?,?)";
        SELECT_USER_PASSWORD = "SELECT * FROM user WHERE username = ? AND password = ?";
        SELECT_USER_SESSION = "SELECT * FROM user WHERE username = ? AND session_id = ?";
        SELECT_USER_UUID = "SELECT * FROM user WHERE unique_id = ?";
        SELECT_USER_USERNAME = "SELECT * FROM user WHERE username = ?";
        UPDATE_SESSION = "UPDATE user SET session_id = ? WHERE unique_id = ?";
        UPDATE_NICKNAME = "UPDATE user SET username = ? WHERE unique_id = ?";
    }

}
