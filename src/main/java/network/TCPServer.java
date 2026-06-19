package network;

import dao.SanPhamDAO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    public static void main(String[] args)
            throws Exception {

        ServerSocket server =
                new ServerSocket(9999);

        while(true){

            Socket socket =
                    server.accept();

            new Thread(() -> {

                try {

                    ObjectInputStream in =
                            new ObjectInputStream(
                                    socket.getInputStream());

                    ObjectOutputStream out =
                            new ObjectOutputStream(
                                    socket.getOutputStream());

                    Request req =
                            (Request) in.readObject();

                    if(req.getAction()
                            .equals("GET_PRODUCTS")) {

                        SanPhamDAO dao =
                                new SanPhamDAO();

                        out.writeObject(
                                new Response(
                                        true,
                                        dao.getAll()
                                )
                        );
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();
        }
    }
}