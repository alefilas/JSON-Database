package server.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.JSON.Answer;
import server.db.DAO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Session implements Runnable {

    private final Socket socket;
    private final DAO DATA_BASE;
    private final Gson gson;
    private final ServerSocket serverSocket;

    public Session(Socket socket, DAO dataBase, ServerSocket serverSocket) {
        this.socket = socket;
        this.DATA_BASE = dataBase;
        this.gson = new Gson();
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {

        try (
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        ) {

            String receivedMsg = in.readUTF();

            if (receivedMsg.contains("exit")) {
                out.writeUTF(gson.toJson(new Answer("OK")));
                serverSocket.close();
                socket.close();
                return;
            }

            try {
                out.writeUTF(executeQuery(receivedMsg));
            } catch (Exception e) {
                out.writeUTF(e.getMessage());
                e.printStackTrace();
            }

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String executeQuery(String clientQuery) {

        JsonObject object = gson.fromJson(clientQuery, JsonObject.class);

        return switch (object.get("type").getAsString()) {
            case "get" -> DATA_BASE.get(object.get("key"));
            case "set" -> DATA_BASE.set(object.get("key"), object.get("value"));
            case "delete" -> DATA_BASE.delete(object.get("key"));
            default -> "ERROR";
        };
    }
}
