package network;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.RequestDTO;
import dto.UserDTO;
import service_impl.AuthorizationServiceImpl;
import service_impl.RegistrationServiceImpl;
import service_impl.WorkWithClothesImpl;

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
            case AUTHORIZATION: {
                UserDTO dateSet = (UserDTO) request.getData();
                AuthorizationServiceImpl authorizationService = new AuthorizationServiceImpl();
                return authorizationService.authorization(dateSet);
            }
            case FULL_LIST_USERS: {
                AuthorizationServiceImpl authorizationService = new AuthorizationServiceImpl();
                return authorizationService.getListUsers();
            }
            case DELETE_USER: {
                String dataset = (String) request.getData();
                AuthorizationServiceImpl authorizationService = new AuthorizationServiceImpl();
                return authorizationService.deleteUser(dataset);
            }
            case ADD_USER: {
                UserDTO dataset = (UserDTO) request.getData();
                RegistrationServiceImpl registrationService = new RegistrationServiceImpl();
                return registrationService.registration(dataset);
            }
            case GET_CLOTHES: {
                WorkWithClothesImpl workWithClothes = new WorkWithClothesImpl();
                return workWithClothes.geListClothes();
            }
            case GET_ID: {
                String dataset = (String) request.getData();
                AuthorizationServiceImpl authorizationService = new AuthorizationServiceImpl();
                return authorizationService.getID(dataset);
            }
            case REQUEST_CLOTHES: {
                RequestDTO dataset = (RequestDTO) request.getData();
                WorkWithClothesImpl workWithClothes = new WorkWithClothesImpl();
                return workWithClothes.createRequestOnTheClothes(dataset);
            }
            case REQUEST_BUDGET: {
                RequestDTO dataset = (RequestDTO) request.getData();
                WorkWithClothesImpl workWithClothes = new WorkWithClothesImpl();
                return workWithClothes.createRequestOnTheBudget(dataset);
            }
            default: {
                return new Response(-1, null);
            }
        }

    }
}