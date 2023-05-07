package it.raniero.schoolchat.database.connection;

import it.raniero.schoolchat.database.connection.utils.ConnectionDetails;

import java.sql.Connection;

public interface IConnection {


    void initialize(ConnectionDetails details);

    void close();

    Connection getConnection();

}