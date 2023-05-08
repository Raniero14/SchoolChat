package it.raniero.schoolchat.server.packet;

import it.raniero.schoolchat.api.server.packet.IPacket;
import it.raniero.schoolchat.api.server.packet.IPacketHandler;
import it.raniero.schoolchat.api.user.connection.ISocketWrapper;
import it.raniero.schoolchat.server.packet.handlers.ChatPacketHandler;
import it.raniero.schoolchat.server.packet.handlers.ClientSettingsHandler;
import it.raniero.schoolchat.server.packet.handlers.LoginPacketHandler;
import it.raniero.schoolchat.server.packet.in.*;
import it.raniero.schoolchat.server.packet.out.ServerChatMessagePacket;
import it.raniero.schoolchat.server.packet.out.ServerGeneralResponsePacket;
import it.raniero.schoolchat.server.packet.out.ServerLoginResponsePacket;

import java.util.*;

public class PacketManager {
    private static LinkedList<Class<? extends IPacket>> clientPacketRegister;

    private static LinkedList<Class<? extends IPacket>> clientLoginPacketRegister;
    private static LinkedList<Class<? extends IPacket>> serverPacketRegister;

    private final Map<Class<? extends IPacket>, IPacketHandler> packetHandlers = new HashMap<>();


    public void registerPacketHandlers() {

        LoginPacketHandler loginPacketHandler = new LoginPacketHandler();
        ChatPacketHandler chatPacketHandler = new ChatPacketHandler();

        packetHandlers.put(ClientLoginPacket.class,loginPacketHandler);
        packetHandlers.put(ClientSessionPacket.class,loginPacketHandler);
        packetHandlers.put(ClientRegisterPacket.class,loginPacketHandler);

        packetHandlers.put(ClientChatMessagePacket.class,chatPacketHandler);
        packetHandlers.put(ClientJoinRoomPacket.class,chatPacketHandler);

        packetHandlers.put(ClientCreateRoomPacket.class,new ClientSettingsHandler());

    }

    public void handlePacket(ISocketWrapper wrapper, IPacket packet) {
        if(packetHandlers.containsKey(packet.getClass())) {

            packetHandlers.get(packet.getClass()).handle(wrapper, packet);

        }
    }

    public IPacket createPacket(String input, boolean user) {

        int id = extractId(input);

        int maxId = user ? clientPacketRegister.size() : clientLoginPacketRegister.size();

        if(id == -1 || maxId <= id - 1) {
            return null;
        }

        try {


            Class<? extends IPacket> packetClass = user ? clientPacketRegister.get(id - 1) : clientLoginPacketRegister.get(id - 1);

            System.out.println("trovato pacchetto: " + packetClass.getSimpleName());

            IPacket packet = packetClass.getDeclaredConstructor().newInstance();
            packet.decode(extractContent(input));

            return packet;
        } catch (Exception e) {

            System.out.println("LOGGER STILL NEEDS TO BE IMPLEMENTED, ERROR IN PACKET DECODING");
            return null;

        }

    }

    private String[] extractContent(String input) {
        String[] array = input.split(";");
        return Arrays.copyOfRange(array,1,array.length);
    }

    private int extractId(String input) {
        String[] array = input.split(";");

        try {

            return Integer.parseInt(array[0]);

        } catch (NumberFormatException ex) {

            return -1;

        }
    }

    public int getServerPacketId(IPacket packet) {
        return serverPacketRegister.indexOf(packet.getClass()) + 1;
    }

    static {
        clientPacketRegister = new LinkedList<>(
                Arrays.asList(
                        ClientChatMessagePacket.class,
                        ClientCreateRoomPacket.class,
                        ClientJoinRoomPacket.class,
                        ClientNicknameChangePacket.class));

        clientLoginPacketRegister = new LinkedList<>(
                Arrays.asList(
                        ClientLoginPacket.class,
                        ClientSessionPacket.class,
                        ClientRegisterPacket.class
                ));

        serverPacketRegister = new LinkedList<>(
                Arrays.asList(
                       ServerLoginResponsePacket.class,
                       ServerGeneralResponsePacket.class,
                       ServerChatMessagePacket.class
                ));
    }


}
