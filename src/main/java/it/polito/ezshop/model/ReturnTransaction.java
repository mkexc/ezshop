package it.polito.ezshop.model;

import it.polito.ezshop.data.TicketEntry;

import java.util.List;

public class ReturnTransaction extends BalanceOperation implements it.polito.ezshop.data.SaleTransaction{
    private int quantity;
    private List<TicketEntry> returnedEntries;
    private Integer idSaleTransaction;
    private Integer returnTransactionId;
    private double discountRate;
    private double returnedPrice;

    public ReturnTransaction(int quantity, List<TicketEntry> returnedEntries, Integer idSaleTransaction, Integer returnTransactionId, double discountRate, double returnedPrice){
        super();
        this.quantity=quantity;
        this.idSaleTransaction=idSaleTransaction;
        this.returnedEntries=returnedEntries;
        this.returnTransactionId=returnTransactionId;
        this.discountRate=discountRate;
        this.returnedPrice=returnedPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getIdSaleTransaction() {
        return idSaleTransaction;
    }

    public void setIdSaleTransaction(Integer idSaleTransaction) {
        this.idSaleTransaction = idSaleTransaction;
    }

    @Override
    public Integer getTicketNumber() {
        return returnTransactionId;
    }

    @Override
    public void setTicketNumber(Integer ticketNumber) {
        this.returnTransactionId = ticketNumber;
    }

    @Override
    public List<TicketEntry> getEntries() {
        return returnedEntries;
    }

    @Override
    public void setEntries(List<TicketEntry> entries) {
        this.returnedEntries = entries;
    }

    @Override
    public double getDiscountRate() {
        return discountRate;
    }

    @Override
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public double getPrice() {
        return returnedPrice;
    }

    @Override
    public void setPrice(double price) {
        this.returnedPrice = price;
    }
}
