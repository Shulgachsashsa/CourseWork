package service_impl;

import connect.Response;
import dto.FinancialHistoryDTO;
import entity.FinancialHistory;
import entity.Request;
import entity.User;
import workWithHibernate.TransactionHibernate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkWithBudgetImpl {

    public Response getBudget() {
        try {
            return new Response(1, TransactionHibernate.getBudget());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Response getTotalPrice(Long idReq) {
        try {
            Request request = TransactionHibernate.getClothesByIdReq(idReq);
            String total = request.getClothesDetails();
            double totalSum = 0.0;
            String[] products = total.split(";");
            Pattern pattern = Pattern.compile("totalprice=([\\d,]+)");
            for (String product : products) {
                Matcher matcher = pattern.matcher(product);
                if (matcher.find()) {
                    String priceStr = matcher.group(1).replace(",", ".");
                    double price = Double.parseDouble(priceStr);
                    totalSum += price;
                }
            }
            return new Response(1, totalSum);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Response addBudget(Double totalPrice) {
        if (TransactionHibernate.addBudget(totalPrice))
            return new Response(1, null);
        else
            return new Response(0, null);
    }

    public Response saveFinancial(FinancialHistoryDTO financialHistoryDTO) {
        User user = TransactionHibernate.findByID(financialHistoryDTO.getProcessedBy());
        Request request = TransactionHibernate.getClothesByIdReq(financialHistoryDTO.getRequestId());
        FinancialHistory financialHistory = new FinancialHistory(
          request,
          financialHistoryDTO.getAmountChange(),
          financialHistoryDTO.getNewBalance(),
          user,
          financialHistoryDTO.getComment()
        );
        TransactionHibernate.saveFinancialHistory(financialHistory);
        return new Response(1, null);
    }
}
