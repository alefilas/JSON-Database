package server;


import server.server.Server;

public class Main {

    public static void main(String[] args) {

        Server server = new Server(23456);
        server.start();
    }
}
