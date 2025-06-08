package service_impl;

import connect.Response;
import dto.RequestDTO;
import dto.RequestHistoryDTO;
import dto.UserDTO;
import entity.Request;
import entity.RequestHistory;
import entity.User;
import enums.RequestStatus;
import enums.RequestType;
import org.hibernate.Transaction;
import workWithHibernate.TransactionHibernate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkWithReqHistory {

    public Response getListReqById(Long id) {
        try {
            List<RequestHistory> listSQL = TransactionHibernate.getRequestsHistoryByID(id);
            List<RequestHistoryDTO> listDTO = new ArrayList<>();
            for (RequestHistory req : listSQL) {
                RequestHistoryDTO requestHistoryDTO = new RequestHistoryDTO(
                        req.getUser().getId(),
                        req.getAccountantUser() != null ? req.getAccountantUser().getId() : null,
                        req.getType(),
                        req.getAmount(),
                        req.getDetails(),
                        req.getStatus(),
                        req.getCreatedAt(),
                        req.getProcessedAt(),
                        req.getAccountantComment(),
                        req.getReason()
                );
                listDTO.add(requestHistoryDTO);
            }
            System.out.println(listDTO);
            return new Response(1, listDTO);
        } catch (Exception e) {
            return new Response(0, "");
        }
    }

    public Response getListReq() {
        List<Request> list = TransactionHibernate.getRequestsHistoryWithPending(RequestStatus.PENDING);
        if (list.size() == 0)
            return new Response(0, "false");
        else if (list.size() > 0) {
            List<RequestDTO> listDTO = new ArrayList<>();
            for (Request rq : list) {
                listDTO.add(new RequestDTO(rq.getId(),
                        rq.getUser().getId(),
                        rq.getType(),
                        rq.getReason(),
                        rq.getClothesDetails(),
                        rq.getStatus(),
                        rq.getAmount()
                ));
            }
            return new Response(1, listDTO);
        } else
            return new Response(-1, "false");
    }

    public Response setNewStateRequestByID(RequestDTO requestDTO) {
        if (TransactionHibernate.setNewStateRequest(requestDTO.getId(), requestDTO.getRequestStatus()))
            return new Response(1, "");
        else
            return new Response(0, "");
    }

    public Response setNewReqHistory(RequestHistoryDTO requestHistoryDTO) {
        System.out.println(requestHistoryDTO.getCreatedAt());
        User user = TransactionHibernate.findByID(requestHistoryDTO.getAccountantUser());
        if (TransactionHibernate.setNewHistoryReq(user, requestHistoryDTO.getStatus(),
                requestHistoryDTO.getProcessedAt(), requestHistoryDTO.getReason(),
                requestHistoryDTO.getRequestID()))
            return new Response(1, "");
        else
            return new Response(0, "");
    }

    public Response backClothes(String clothes) {
        Map<String, Integer> map = new HashMap<>();
        String[] entries = clothes.split(";");
        for (String entry : entries) {
            String[] keyValuePairs = entry.split(",");
            String type = null;
            Integer quantity = null;
            for (String pair : keyValuePairs) {
                pair = pair.trim();
                String[] keyValue = pair.split("=");
                if (keyValue.length != 2) continue;
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                if (key.equals("type")) {
                    type = value;
                } else if (key.equals("quantity")) {
                    value = value.replace(",", "");
                    quantity = Integer.parseInt(value);
                }
            }
            if (type != null && quantity != null) {
                map.put(type, quantity);
            }
        }
        try {
            TransactionHibernate.backClothes(map);
            return new Response(1, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Response getListReqByIdProcess(Long id) {
        try {
            List<RequestHistory> listSQL = TransactionHibernate.getReqByProcessId(id);
            List<RequestHistoryDTO> listDTO = new ArrayList<>();
            for (RequestHistory req : listSQL) {
                RequestHistoryDTO requestHistoryDTO = new RequestHistoryDTO(
                        req.getUser().getId(),
                        req.getAccountantUser() != null ? req.getAccountantUser().getId() : null,
                        req.getType(),
                        req.getAmount(),
                        req.getDetails(),
                        req.getStatus(),
                        req.getCreatedAt(),
                        req.getProcessedAt(),
                        req.getAccountantComment(),
                        req.getReason()
                );
                listDTO.add(requestHistoryDTO);
            }
            return new Response(1, listDTO);
        } catch (Exception e) {
            return new Response(0, "");
        }
    }
}
