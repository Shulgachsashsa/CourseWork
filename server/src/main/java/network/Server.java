package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 12345;

    public static void main(String args[]) throws IOException {
        ExecutorService pool = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен! Ожидаю подключения...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Подключен клиент! " + clientSocket.getInetAddress());
                pool.execute(new ClientHandler(clientSocket));
            }
        }
    }
}
