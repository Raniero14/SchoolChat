package it.raniero.chat;

import it.raniero.chat.api.ISchoolChat;
import it.raniero.chat.api.utils.StartupSettings;
import lombok.Getter;

public class SchoolChat implements ISchoolChat {



    @Getter
    private static SchoolChat instance;


    @Override
    public void start(StartupSettings settings) {

        instance = this;

    }

    @Override
    public void shutdown(boolean save) {

    }
}
