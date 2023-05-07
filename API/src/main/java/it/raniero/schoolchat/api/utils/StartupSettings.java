package it.raniero.schoolchat.api.utils;


import lombok.Data;

@Data
public class StartupSettings {

    private final String networkInterface;

    private final int port;

    private final boolean useDatabaseConfig;


}
