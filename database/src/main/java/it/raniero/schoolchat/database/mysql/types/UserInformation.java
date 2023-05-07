package it.raniero.schoolchat.database.mysql.types;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class UserInformation {


    private final long userId;

    private final UUID uniqueId;

    private String username;

    private boolean admin;

    private boolean openMessages;



}
