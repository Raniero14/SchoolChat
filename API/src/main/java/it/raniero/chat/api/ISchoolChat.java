package it.raniero.chat.api;

import it.raniero.chat.api.utils.StartupSettings;

public interface ISchoolChat {


    void start(StartupSettings settings);

    void shutdown(boolean save);

}
