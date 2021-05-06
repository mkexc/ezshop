package it.polito.ezshop.model.customerlist;

public class Customer implements it.polito.ezshop.data.Customer {

     private Integer customerId;
     private String loyaltyCardId="";
     private String customerName;
     private int points=0;

     Customer(String customerName) {
         this.customerName=customerName;
     }

    @Override
    public String getCustomerName() {
        return this.customerName;
    }

    @Override
    public void setCustomerName(String customerName) {
        this.customerName=customerName;
    }

    @Override
    public String getCustomerCard() {
        return this.loyaltyCardId;
    }

    @Override
    public void setCustomerCard(String customerCard) {
        this.loyaltyCardId=customerCard;
    }

    @Override
    public Integer getId() {
        return customerId;
    }

    @Override
    public void setId(Integer id) {
        this.customerId=id;
    }

    @Override
    public Integer getPoints() {
        return points;
    }

    @Override
    public void setPoints(Integer points) {
        this.points=points;
    }
}
