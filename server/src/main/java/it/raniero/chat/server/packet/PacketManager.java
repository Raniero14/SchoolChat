package it.raniero.chat.server.packet;

import it.raniero.chat.api.server.packet.IPacket;
import it.raniero.chat.api.server.packet.IPacketHandler;
import it.raniero.chat.api.user.connection.ISocketWrapper;

import java.util.*;

public class PacketManager {
    private static LinkedList<Class<? extends IPacket>> clientPacketRegister;

    private static LinkedList<Class<? extends IPacket>> clientLoginPacketRegister;
    private static LinkedList<Class<? extends IPacket>> serverPacketRegister;

    private final Map<Class<? extends IPacket>, IPacketHandler> packetHandlers = new HashMap<>();


    public void registerPacketHandlers() {


    }

    public void handlePacket(ISocketWrapper wrapper, IPacket packet) {
        if(packetHandlers.containsKey(packet.getClass())) {

            packetHandlers.get(packet.getClass()).handle(wrapper, packet);

        }
    }

    public IPacket createPacket(String input, boolean user) {

        int id = extractId(input);

        int maxId = user ? clientPacketRegister.size() : clientLoginPacketRegister.size();

        if(id == -1 || maxId < id) {
            return null;
        }

        try {


            Class<? extends IPacket> packetClass = user ? clientPacketRegister.get(id - 1) : clientLoginPacketRegister.get(id - 1);

            IPacket packet = packetClass.getDeclaredConstructor().newInstance();
            packet.decode(extractContent(input));

            return packet;
        } catch (Exception e) {

            System.out.println("LOGGER STILL NEEDS TO BE IMPLEMENTED, ERROR IN PACKET DECODING");;
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

    static {
        clientPacketRegister = new LinkedList<>(Arrays.asList());

        clientLoginPacketRegister = new LinkedList<>(Arrays.asList());


        serverPacketRegister = new LinkedList<>(Arrays.asList());
    }


}
