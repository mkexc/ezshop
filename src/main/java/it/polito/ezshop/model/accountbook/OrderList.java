package it.polito.ezshop.model.accountbook;

import java.util.ArrayList;

public class OrderList {
    private  ArrayList<Order> orderList = new ArrayList<>();

    public Integer issueOrder(String productCode, int quantity, double pricePerUnit){
        return 0;
    }

    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit){
        return 0;
    }

    public boolean payOrder(Integer orderId){
        return true;
    }

    public boolean recordOrderArrival(Integer orderId){
        return true;
    }

    private void savePersistent(){}

}
