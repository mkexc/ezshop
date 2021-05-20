package it.polito.ezshop.model;

import it.polito.ezshop.data.TicketEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcceptableSaleTransaction {
    private final List<TicketEntry> entries= new ArrayList<>();
    private final List<TicketEntry> entries2= new ArrayList<>();
    private it.polito.ezshop.model.SaleTransaction sale;

    @BeforeEach
    public void newSaleTransaction(){
        sale = new it.polito.ezshop.model.SaleTransaction(1,  entries, 0.3,100.0);
    }

    @Test
    public void testGetTransactionId() {
        assertEquals(1, sale.getTicketNumber());
    }
    @Test
    public void testGetEntry() {
        assertEquals(entries, sale.getEntries());
    }
    @Test
    public void testGetDiscountRate() {
        assertEquals(0.3, sale.getDiscountRate());
    }
    @Test
    public void testGetPrice() {
        assertEquals(100.0, sale.getPrice());
    }

    @Test
    public void testSetTransactionId() {
        sale.setTicketNumber(2);
        assertEquals(2, sale.getTicketNumber());
    }
    @Test
    public void testSetEntry() {
        sale.setEntries(entries2);
        assertEquals(entries2, sale.getEntries());
    }
    @Test
    public void testSetDiscountRate() {
        sale.setDiscountRate(0.33);
        assertEquals(0.33, sale.getDiscountRate());
    }
    @Test
    public void testSetPrice() {
        sale.setPrice(101.0);
        assertEquals(101.0, sale.getPrice());
    }
}