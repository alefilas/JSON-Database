package server.server;

import server.db.DAO;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int PORT;
    private final DAO DATA_BASE;
    private final ExecutorService service;

    public Server(int PORT) {
        this.PORT = PORT;
        this.DATA_BASE = new DAO();
        this.service = Executors.newCachedThreadPool();
    }


    public void start() {

        System.out.println("Server started!");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {

                service.submit(new Session(serverSocket.accept(), DATA_BASE, serverSocket));

            }
        } catch (IOException e) {
            service.shutdown();
        }
    }
}
