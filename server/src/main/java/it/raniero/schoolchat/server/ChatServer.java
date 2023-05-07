package it.raniero.schoolchat.server;

import it.raniero.schoolchat.api.server.IChatServer;
import it.raniero.schoolchat.api.server.packet.IPacket;
import it.raniero.schoolchat.api.user.IChatUser;
import it.raniero.schoolchat.api.user.connection.ISocketWrapper;
import it.raniero.schoolchat.server.packet.PacketManager;
import it.raniero.schoolchat.server.socket.ClientSocketWrapper;
import lombok.Getter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class ChatServer implements IChatServer {

    private Map<InetAddress, ClientSocketWrapper> activeSockets;

    private ServerSocket serverSocket;

    private Thread serverThread;

    @Getter
    private PacketManager packetManager = new PacketManager();


    @Override
    public void createServer(String network, int port) {

        packetManager.registerPacketHandlers();

        serverThread = new Thread(()-> {

            try {

                serverSocket = createServerSocket(network,port);

                acceptConnections();

            } catch (IOException e) {

                System.out.println("LOGGER STILL NEEDS TO BE IMPLEMENTED, ERROR IN SOCKET CREATION");

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
