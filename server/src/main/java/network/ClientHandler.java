package network;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.FinancialHistoryDTO;
import dto.RequestDTO;
import dto.RequestHistoryDTO;
import dto.UserDTO;
import service_impl.*;

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
            case GET_REQUEST_BY_ID: {
                Long dataset = (Long) request.getData();
                WorkWithReqHistory workWithReqHistory = new WorkWithReqHistory();
                return workWithReqHistory.getListReqById(dataset);
            }
            case MINES_CLOTHES: {
                String data = (String) request.getData();
                WorkWithClothesImpl workWithClothes = new WorkWithClothesImpl();
                return workWithClothes.minesClothes(data);
            }
            case UPDATE_USER_ACCESS: {
                UserDTO userDTO = (UserDTO) request.getData();
                AuthorizationServiceImpl authorizationService = new AuthorizationServiceImpl();
                return authorizationService.editAccess(userDTO);
            }
            case GET_REQUESTS_WITH_STATE_PENDING: {
                WorkWithReqHistory workWithReqHistory = new WorkWithReqHistory();
                return workWithReqHistory.getListReq();
            }
            case SET_STATE_REQUEST: {
                RequestDTO requestDTO = (RequestDTO) request.getData();
                WorkWithReqHistory workWithReqHistory = new WorkWithReqHistory();
                return workWithReqHistory.setNewStateRequestByID(requestDTO);
            }
            case SET_NEW_REQ_HISTORY: {
                RequestHistoryDTO requestHistoryDTO = (RequestHistoryDTO) request.getData();
                WorkWithReqHistory workWithReqHistory = new WorkWithReqHistory();
                return workWithReqHistory.setNewReqHistory(requestHistoryDTO);
            }
            case GET_BUDGET: {
                WorkWithBudgetImpl workWithBudget = new WorkWithBudgetImpl();
                return workWithBudget.getBudget();
            }
            case GET_TOTAL_PRICE_CLOTHES: {
                Long id = (Long) request.getData();
                WorkWithBudgetImpl workWithBudget = new WorkWithBudgetImpl();
                return workWithBudget.getTotalPrice(id);
            }
            case ADD_BUDGET: {
                Double total = (Double) request.getData();
                WorkWithBudgetImpl workWithBudget = new WorkWithBudgetImpl();
                return workWithBudget.addBudget(total);
            }
            case SAVE_FINANCIAL_HISTORY: {
                FinancialHistoryDTO financialHistoryDTO = (FinancialHistoryDTO) request.getData();
                WorkWithBudgetImpl workWithBudget = new WorkWithBudgetImpl();
                return workWithBudget.saveFinancial(financialHistoryDTO);
            }
            case BACK_CLOTHES: {
                String clothes = (String) request.getData();
                WorkWithReqHistory workWithReqHistory = new WorkWithReqHistory();
                return workWithReqHistory.backClothes(clothes);
            }
            default: {
                return new Response(-1, null);
            }
        }

    }
}