package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {


    private final String ADDRESS;
    private final int PORT;

    public Client(String address, int port) {
        this.ADDRESS = address;
        this.PORT = port;
    }

    public void connect(String query) {

        System.out.println("Client started!");

        try (
                Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                )
        {
            out.writeUTF(query);
            System.out.println("Sent: " + query);

            String receivedMsg = in.readUTF();
            System.out.println("Received: " + receivedMsg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
