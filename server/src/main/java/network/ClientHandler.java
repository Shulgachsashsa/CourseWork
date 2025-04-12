package network;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.RegistrationDTO;
import service_impl.RegistrationServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
            Request request = (Request) in.readObject();
            Response response = processRequest(request);
            out.writeObject(response);
            out.flush();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response processRequest(Request request) {
        CommandType commandType = request.getCommand();
        switch (commandType) {
            case REGISTRATION: {
                RegistrationDTO dateSet = (RegistrationDTO) request.getData();
                RegistrationServiceImpl registrationService = new RegistrationServiceImpl();
                return registrationService.registration(dateSet);
            }
            case AUTHORIZATION: {

            }
            default: {
                return new Response(-1, null);
            }
        }

    }
}