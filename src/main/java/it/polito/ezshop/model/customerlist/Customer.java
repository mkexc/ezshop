package it.polito.ezshop.model.customerlist;

public class Customer implements it.polito.ezshop.data.Customer {

     private Integer customerId;
     private String loyaltyCardId;
     private String name;
     private String surname;

    @Override
    public String getCustomerName() {
        return this.name;
    }

    @Override
    public void setCustomerName(String customerName) {
        this.name=customerName;
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

        return null;
    }

    @Override
    public void setPoints(Integer points) {

    }
}
