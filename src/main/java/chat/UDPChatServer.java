package chat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPChatServer {

    public static void main(String[] args)
            throws Exception {

        DatagramSocket socket =
                new DatagramSocket(8888);

        byte[] buffer =
                new byte[1024];

        while(true){

            DatagramPacket packet =
                    new DatagramPacket(
                            buffer,
                            buffer.length
                    );

            socket.receive(packet);

            String msg =
                    new String(
                            packet.getData(),
                            0,
                            packet.getLength()
                    );

            System.out.println(msg);
        }
    }
}