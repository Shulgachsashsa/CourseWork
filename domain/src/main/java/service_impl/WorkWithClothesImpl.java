package service_impl;

import connect.Response;
import dto.ClothesDTO;
import dto.RequestDTO;
import entity.Clothes;
import entity.Request;
import entity.RequestHistory;
import workWithHibernate.TransactionHibernate;

import java.util.ArrayList;
import java.util.List;

public class WorkWithClothesImpl {

    public Response geListClothes() {
        List<ClothesDTO> clothes = new ArrayList<>();
        List<Clothes> clothesList = TransactionHibernate.getClothes();
        for (Clothes cl: clothesList) {
            ClothesDTO clothesDTO = new ClothesDTO(cl.getCounter(), cl.getClothesType(), cl.getPrice());
            clothes.add(clothesDTO);
        }
        return new Response(1, clothes);
    }

    public Response createRequestOnTheClothes(RequestDTO requestDTO) {
        try {
            Request request = new Request(TransactionHibernate.findByID(requestDTO.getUserId()),
                    requestDTO.getRequestType(), requestDTO.getClothesDetails(), requestDTO.getReason(),
                    requestDTO.getRequestStatus());
            TransactionHibernate.createRequestOnTheClothes(request);
            RequestHistory requestHistory = new RequestHistory(
                    TransactionHibernate.findByID(requestDTO.getUserId()),
                    requestDTO.getRequestType(),
                    requestDTO.getClothesDetails(),
                    requestDTO.getRequestStatus(),
                    requestDTO.getReason(),
                    request);
            TransactionHibernate.addReqOnTheLog(requestHistory);
            return new Response(1, "");
        } catch (Exception e) {
            return new Response(0, "");
        }
    }

    public Response createRequestOnTheBudget(RequestDTO requestDTO) {
        try {
            Request request = new Request(TransactionHibernate.findByID(requestDTO.getUserId()),
                    requestDTO.getRequestType(), requestDTO.getAmount(), requestDTO.getReason(),
                    requestDTO.getRequestStatus());
            TransactionHibernate.createRequestOnTheBudget(request);
            RequestHistory requestHistory = new RequestHistory(
                    TransactionHibernate.findByID(requestDTO.getUserId()),
                    requestDTO.getRequestType(),
                    requestDTO.getAmount(),
                    requestDTO.getRequestStatus(),
                    requestDTO.getReason(),
                    request);
            TransactionHibernate.addReqOnTheLog(requestHistory);
            return new Response(1, "");
        } catch (Exception e) {
            return new Response(0, "");
        }
    }

    public Response minesClothes(String clothes) {
        if (TransactionHibernate.reqMinesClothes(clothes) == true)
            return new Response(1, "true");
        else
            return new Response(-1, "false");
    }

}
