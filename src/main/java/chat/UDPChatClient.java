package chat;

import java.net.*;

public class UDPChatClient {

    public static void send(String msg)
            throws Exception {

        DatagramSocket socket =
                new DatagramSocket();

        byte[] data = msg.getBytes();

        DatagramPacket packet =
                new DatagramPacket(
                        data,
                        data.length,
                        InetAddress.getByName("localhost"),
                        8888
                );

        socket.send(packet);
    }
}