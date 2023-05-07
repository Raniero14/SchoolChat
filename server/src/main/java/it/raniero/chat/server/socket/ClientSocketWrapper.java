package it.raniero.chat.server.socket;

import it.raniero.chat.api.server.packet.IPacket;
import it.raniero.chat.api.user.connection.ISocketWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.UUID;

@RequiredArgsConstructor
public class ClientSocketWrapper implements ISocketWrapper {


    private Thread inputThread;

    @Getter
    private final Socket socket;


    private UUID uuid;


    @Override
    public void listen() {

        inputThread = new Thread(() -> {
            try {
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (!Thread.currentThread().isInterrupted() || socket.isClosed() || socket.isInputShutdown()) {

                    String line = reader.readLine();

                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public void sendPacket(IPacket packet) {

    }

    @Override
    public UUID getPairedUser() {
        return uuid;
    }
}
