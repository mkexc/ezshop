package it.polito.ezshop.model.customerlist;

import java.util.ArrayList;
import java.util.List;

//extends LoyaltyCardList possible solution
public class CustomerList {
    private ArrayList<Customer> customerList = new ArrayList<>();

    public Integer defineCustomer(String customerName) {
        return 0;
    }

    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) {
        return true;
    }

    public boolean deleteCustomer(Integer id){
        return true;
    }

    public Customer getCustomer(Integer id){
        return null;
    }

    public List<Customer> getAllCustomers(){
        return customerList;
    }

    public boolean attachCardToCustomer(String customerCard, Integer customerId){
        return true;
    }

    private void savePersistent(){}
}