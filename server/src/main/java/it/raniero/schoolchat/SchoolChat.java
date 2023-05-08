package it.raniero.schoolchat;

import it.raniero.schoolchat.api.ISchoolChat;
import it.raniero.schoolchat.api.user.action.UserAction;
import it.raniero.schoolchat.api.utils.StartupSettings;
import it.raniero.schoolchat.chat.ChatHandler;
import it.raniero.schoolchat.database.connection.impl.HikariConnection;
import it.raniero.schoolchat.database.connection.utils.ConnectionDetails;
import it.raniero.schoolchat.database.mysql.ChatMessageDao;
import it.raniero.schoolchat.database.mysql.ChatRoomDao;
import it.raniero.schoolchat.database.mysql.ChatUserDao;
import it.raniero.schoolchat.server.ChatServer;
import it.raniero.schoolchat.user.manager.ChatUserManager;
import lombok.Getter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SchoolChat implements ISchoolChat {



    @Getter
    private static SchoolChat instance;

    @Getter
    private ChatServer server;

    private ChatHandler chatHandler;

    @Getter
    private ChatRoomDao chatRoomDao;

    @Getter
    private ChatUserDao chatUserDao;

    @Getter
    private ChatMessageDao chatMessageDao;

    @Getter
    private ChatUserManager userManager;

    private final BlockingQueue<UserAction> actionQueue = new LinkedBlockingQueue<>();

    private Thread actionThread;

    @Override
    public void start(StartupSettings settings) {
        instance = this;

        initDatabase();
        userManager = new ChatUserManager();
        chatHandler = new ChatHandler();
        chatHandler.init();
        server = new ChatServer();
        server.createServer(settings.getNetworkInterface(),settings.getPort());

        actionThread = new Thread(this::checkActionQueue);
        actionThread.start();

        chatUserDao.registerUser("prova","ciao123");
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

                chatHandler.handleAction(action);
                userManager.handleAction(action);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

            }
        }
    }

    public void initDatabase() {
        HikariConnection connection = new HikariConnection();
        connection.initialize(new ConnectionDetails("localhost",3306,"chat","root",""));

        chatRoomDao = new ChatRoomDao(connection);
        chatUserDao = new ChatUserDao(connection);
        chatMessageDao = new ChatMessageDao(connection);

    }

    @Override
    public void shutdown(boolean save) {

    }
}
