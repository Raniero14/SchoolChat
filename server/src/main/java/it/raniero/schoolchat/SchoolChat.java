package it.raniero.schoolchat;

import it.raniero.schoolchat.api.ISchoolChat;
import it.raniero.schoolchat.api.user.action.UserAction;
import it.raniero.schoolchat.api.utils.StartupSettings;
import it.raniero.schoolchat.server.ChatServer;
import lombok.Getter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SchoolChat implements ISchoolChat {



    @Getter
    private static SchoolChat instance;

    @Getter
    private ChatServer server;

    private final BlockingQueue<UserAction> actionQueue = new LinkedBlockingQueue<>();

    private Thread actionThread;

    @Override
    public void start(StartupSettings settings) {

        instance = this;

        server = new ChatServer();

        actionThread = new Thread(() -> {

        });

        actionThread.start();
    }


    public void submitAction(UserAction action) {

        try {

            actionQueue.put(action);

        } catch (InterruptedException e) {

            e.printStackTrace();

        }
    }

    public void checkActionQueue() {
        while (!Thread.currentThread().isInterrupted()) {
            try {

                UserAction action = actionQueue.take();



            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

            }
        }
    }

    @Override
    public void shutdown(boolean save) {

    }
}
