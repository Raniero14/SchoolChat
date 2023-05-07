package it.raniero.schoolchat.database.connection.utils;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ConnectionDetails {


    private final String hostname;

    private final int port;

    private final String database;

    private final String username;

    private final String password;

}