package it.raniero.schoolchat.database;

import it.raniero.schoolchat.database.connection.utils.LoginStatus;
import it.raniero.schoolchat.database.mysql.types.UserInformation;

import java.util.UUID;

public interface IUserDao {


    void createTables();
    UUID registerUser(String username,String password);

    LoginStatus loginUser(String username, String password);

    LoginStatus loginUserFromSession(String username, String session);

    UserInformation getUser(String username);

    UserInformation getUser(UUID uuid);
    boolean changeUsername(UUID uuid,String selectedUsername);




}
