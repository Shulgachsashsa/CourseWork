package mainclient;

import frames.MainFrame;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientMain {
    private static Socket socket;

    public ClientMain(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            MainFrame.createAndShowGUI();
        } catch (IOException e) {
            System.err.println("Ошибка подключения к серверу: " + e.getMessage());
            System.exit(1);
        }
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void main(String[] args) {
        new ClientMain("localhost", 12345);
    }
}
