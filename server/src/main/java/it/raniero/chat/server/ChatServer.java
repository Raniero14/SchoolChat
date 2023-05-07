package it.raniero.chat.server;

import it.raniero.chat.SchoolChat;
import it.raniero.chat.api.server.IChatServer;
import it.raniero.chat.api.server.packet.IPacket;
import it.raniero.chat.api.user.IChatUser;
import it.raniero.chat.api.user.connection.ISocketWrapper;
import it.raniero.chat.server.packet.PacketManager;
import it.raniero.chat.server.socket.ClientSocketWrapper;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.UUID;

public class ChatServer implements IChatServer {

    private Map<InetAddress, ClientSocketWrapper> activeSockets;

    private ServerSocket serverSocket;

    private Thread serverThread;

    private PacketManager packetManager = new PacketManager();


    @Override
    public void createServer(String network, int port) {
        serverThread = new Thread(()-> {

            try {

                serverSocket = createServerSocket(network,port);

                acceptConnections();

            } catch (IOException e) {

                System.out.println("LOGGER STILL NEEDS TO BE IMPLEMENTED, ERROR IN SOCKET CREATION");;

            }

        });

        serverThread.start();
    }

    @Override
    public void shutdownServer(String network, int port) {

        serverThread.interrupt();
    }

    @Override
    public void broadcastPacket(IPacket packet) {

    }

    @Override
    public void sendPacket(IChatUser user, IPacket packet) {

    }

    @Override
    public void handleMessage(ISocketWrapper wrapper, String message) {

        IPacket packet = packetManager.createPacket(message, wrapper.getPairedUser() != null);

        if(packet == null) {
            return;
        }

        packetManager.handlePacket(wrapper,packet);

    }


    private void acceptConnections() {
        while (!Thread.currentThread().isInterrupted()) {
            try {

                Socket socket = serverSocket.accept();

                activeSockets.put(socket.getInetAddress(),new ClientSocketWrapper(socket));

            } catch (IOException e) {

                System.out.println("Errore client: connessione chiusa");

            }

        }
    }

    private ServerSocket createServerSocket(String network,int port) throws IOException {
        return new ServerSocket(port,50, InetAddress.getByName(network));
    }

}
