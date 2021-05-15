package it.polito.ezshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcceptableTicketEntry {
    private it.polito.ezshop.model.TicketEntry ticketEntry;

    @BeforeEach
    public void newEntry(){
        ticketEntry = new it.polito.ezshop.model.TicketEntry("11234567890125", "product", 123, 3.70, 1.0 );
    }


    @Test
    public void testGetBarcode() {
        assertEquals("11234567890125", ticketEntry.getBarCode());
    }

    @Test
    public void testGetProductDescription() {
        assertEquals("product", ticketEntry.getProductDescription());
    }

    @Test
    public void testGetAmount() {
        assertEquals(123, ticketEntry.getAmount());
    }

    @Test
    public void testGetPricePerUnit() {
        assertEquals(3.70, ticketEntry.getPricePerUnit());
    }
    @Test
    public void testGetDiscountRate() {
        assertEquals(1.0, ticketEntry.getDiscountRate());
    }

    @Test
    public void testSetBarcode() {
        ticketEntry.setBarCode("2143325343648");
        assertEquals("2143325343648", ticketEntry.getBarCode());
    }

    @Test
    public void testSetProductDescription() {
        ticketEntry.setProductDescription("prodotto");
        assertEquals("prodotto", ticketEntry.getProductDescription());
    }

    @Test
    public void testSetAmount() {
        ticketEntry.setAmount(555);
        assertEquals(555, ticketEntry.getAmount());
    }

    @Test
    public void testSetPricePerUnit() {
        ticketEntry.setPricePerUnit(8.88);
        assertEquals(8.88, ticketEntry.getPricePerUnit());
    }
    @Test
    public void testSetDiscountRate() {
        ticketEntry.setDiscountRate(0.0);
        assertEquals(0.0, ticketEntry.getDiscountRate());
    }
}
