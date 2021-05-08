package it.polito.ezshop.model.customerlist;

import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//extends LoyaltyCardList possible solution
public class CustomerList {
    private List<Customer> customerList=new ArrayList<>();

//    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException {
//
//        if (customerName!=null && !customerName.equals("") && customerList.stream().noneMatch(c-> c.getCustomerName().equals(customerName))){
//            Customer c = new Customer(customerName);
//            // TODO qua creo una riga nel db, prendo il nuovo id e faccio customer.setId()
//            customerList.add(c);
//        }
//        else{
//            throw new InvalidCustomerNameException();
//        }
//        // TODO restituire l'id
//        return 0;
//    }
    public Integer defineCustomer(Integer customerId, String loyaltyCardId, String customerName, Integer points) throws InvalidCustomerNameException {

        if (customerName!=null && !customerName.equals("") && customerList.stream().noneMatch(c-> c.getCustomerName().equals(customerName))){
            Customer c = new Customer(customerId,loyaltyCardId,customerName,points);
            // TODO qua creo una riga nel db, prendo il nuovo id e faccio customer.setId()
            customerList.add(c);
        }
        else{
            throw new InvalidCustomerNameException();
        }
        // TODO restituire l'id
        return 0;
    }

    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException{
        Customer c = this.getCustomer(id);

        c.setCustomerName(newCustomerName);
        if(newCustomerCard!=null) {
            if(newCustomerCard.length()==10)
                c.setCustomerCard(newCustomerCard);
            else
                throw new InvalidCustomerCardException();
        }
        else {
            throw new InvalidCustomerCardException();
        }

        return true;
    }

    public boolean deleteCustomer(Integer id){

        return true;
    }

    public Customer getCustomer(Integer id){
        List<Customer> cTmp = customerList.stream().filter(c -> c.getId().equals(id)).collect(Collectors.toList());
        Customer c = null;
        if(cTmp.size()!=0){
            c=cTmp.get(0);
        }
        return c;
    }

    public List<Customer> getAllCustomers(){
        return customerList;
    }

    public String createCard() throws UnauthorizedException{
        return "";
    }

    public boolean attachCardToCustomer(String customerCard, Integer customerId){
        return true;
    }

    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException{
        return true;
    }

    public String toString(){
        String res="";
        for(Customer c : customerList){
            res+="name: " + c.getCustomerName() + "\n" ;
        }
        return res;
    }
    private void savePersistent(){}
}