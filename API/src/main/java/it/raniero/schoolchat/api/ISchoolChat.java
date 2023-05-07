package it.raniero.schoolchat.api;

import it.raniero.schoolchat.api.utils.StartupSettings;

public interface ISchoolChat {


    void start(StartupSettings settings);

    void shutdown(boolean save);

}
