package network;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.UserDTO;
import service_impl.AuthorizationServiceImpl;
import service_impl.RegistrationServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
            while (!clientSocket.isClosed()) {
                try {
                    Request request = (Request) in.readObject();
                    Response response = processRequest(request);
                    out.writeObject(response);
                } catch (ClassNotFoundException e) {
                    System.err.println("Ошибка десериализации запроса: " + e.getMessage());
                    break;
                } catch (SocketException e) {
                    System.err.println("Клиент " + clientSocket.getInetAddress() + " отключился!");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response processRequest(Request request) {
        CommandType commandType = request.getCommand();
        switch (commandType) {
            case REGISTRATION: {
                UserDTO dateSet = (UserDTO) request.getData();
                RegistrationServiceImpl registrationService = new RegistrationServiceImpl();
                return registrationService.registration(dateSet);
            }
            case AUTHORIZATION: {
                UserDTO dateSet = (UserDTO) request.getData();
                AuthorizationServiceImpl authorizationService = new AuthorizationServiceImpl();
                return authorizationService.authorization(dateSet);
            }
            case FULL_LIST_USERS: {
                UserDTO dataset = (UserDTO) request.getData();
                AuthorizationServiceImpl authorizationService = new AuthorizationServiceImpl();
                return authorizationService.getListUsers();
            }
            default: {
                return new Response(-1, null);
            }
        }

    }
}