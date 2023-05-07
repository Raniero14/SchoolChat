package it.raniero.schoolchat.application;

import it.raniero.schoolchat.SchoolChat;
import it.raniero.schoolchat.api.utils.StartupSettings;

public class SchoolChatApplication  {

    public static void main(String[] args) {
        new SchoolChat().start(new StartupSettings("0.0.0.0",4020,false));
    }

}
