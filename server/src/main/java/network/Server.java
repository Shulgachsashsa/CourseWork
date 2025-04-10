package network;

import controller.RegController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int port = 12345;


    public static void main(String args[]) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен! Ожидаю подключения...");
            ExecutorService pool = Executors.newCachedThreadPool();
            RegController regController = new RegController();
            while (true) {
                try (Socket clientSocket = serverSocket.accept()){
                    System.out.println("Подключен клиент! " + clientSocket.getInetAddress());
                    pool.execute(new ClientHandler(clientSocket));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
