package it.raniero.schoolchat.database.connection.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class LoginStatus {

    private final UUID uuid;

    private final String sessionToken;


}
