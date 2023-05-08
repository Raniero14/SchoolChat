package it.raniero.schoolchat.server.socket;

import it.raniero.schoolchat.SchoolChat;
import it.raniero.schoolchat.api.server.packet.IPacket;
import it.raniero.schoolchat.api.user.connection.ISocketWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RequiredArgsConstructor
public class ClientSocketWrapper implements ISocketWrapper {


    private Thread inputThread;

    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    private Thread outputThread;

    @Getter
    private final Socket socket;

    private UUID uuid;


    @Override
    public void listen() {

        inputThread = new Thread(() -> {
            try {
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (!Thread.currentThread().isInterrupted() || !socket.isClosed() || !socket.isInputShutdown()) {

                    String line = reader.readLine();

                    System.out.println("Nuovo pacchetto: " + line);
                    SchoolChat.getInstance().getServer().handleMessage(this,line);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        outputThread = new Thread(() -> {
            try {

                checkSendQueue(socket.getOutputStream());

            } catch (IOException e) { e.printStackTrace(); }
        });

        inputThread.start();
        outputThread.start();

    }

    @Override
    public void disconnect(String message) {

        inputThread.interrupt();


    }


    @Override
    public void sendPacket(IPacket packet) {
        int id = SchoolChat.getInstance().getServer().getPacketManager().getServerPacketId(packet);
        try {

            messageQueue.put(id + ";" + packet.encode());

        } catch (InterruptedException e) {

            //Non dovrebbe farlo in teoria, li aggiungo nel main thread lol
            e.printStackTrace();

        }
    }

    public void checkSendQueue(OutputStream output) {
        while (!Thread.currentThread().isInterrupted() || !socket.isClosed() || !socket.isOutputShutdown()) {
            try {

                String message = messageQueue.take();
                output.write(message.getBytes(StandardCharsets.UTF_8));
                output.write("\n".getBytes(StandardCharsets.UTF_8));
                output.flush();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public UUID getPairedUser() {
        return uuid;
    }

    @Override
    public void pairUser(UUID uuid) {
        this.uuid = uuid;
    }
}
