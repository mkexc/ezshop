package it.polito.ezshop.model.accountbook;

import it.polito.ezshop.data.TicketEntry;

import java.util.List;

public class SaleTransaction implements it.polito.ezshop.data.SaleTransaction {

    @Override
    public Integer getTicketNumber() {
        return null;
    }

    @Override
    public void setTicketNumber(Integer ticketNumber) {

    }

    @Override
    public List<TicketEntry> getEntries() {
        //lista dei prodotti entrati nella transazione/scontrino
        return null;
    }

    @Override
    public void setEntries(List<TicketEntry> entries) {

    }

    @Override
    public double getDiscountRate() {
        return 0;
    }

    @Override
    public void setDiscountRate(double discountRate) {

    }

    @Override
    public double getPrice() {
        return 0;
    }

    @Override
    public void setPrice(double price) {

    }
}
