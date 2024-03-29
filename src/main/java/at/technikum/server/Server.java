package at.technikum.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket server;
    private final ServerApplication app;

    public Server(ServerApplication app) {
        this.app = app;
    }

    //The server listens for incoming connections using a ServerSocket
    //When a connection is accepted (server.accept()), it creates a new RequestHandler object to process the request.
    //It then wraps the RequestHandler in a Thread object and starts the thread (thread.start()).

    public void start() {
        try {
            server = new ServerSocket(10001);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Server started on http://localhost:10001");

        while (true) {
            try {
                Socket client = server.accept();

                RequestHandler handler = new RequestHandler(client, app);
                Thread thread = new Thread(handler);
                thread.start();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
