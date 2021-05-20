package it.polito.ezshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcceptableCustomer {
    private it.polito.ezshop.model.Customer customer;

    @BeforeEach
    public void newCustomer(){
        customer = new it.polito.ezshop.model.Customer(1, "Pinuccio", "2143254542", 370);
    }

    @Test
    public void testGetId() {
        assertEquals(1, customer.getId());
    }

    @Test
    public void testGetLoyaltyCardId() {
        assertEquals("2143254542", customer.getCustomerCard());
    }

    @Test
    public void testGetCustomerName(){
        assertEquals("Pinuccio", customer.getCustomerName());
    }

    @Test
    public void testGetPoints(){
        assertEquals(370, customer.getPoints());
    }

    @Test
    public void testSetId() {
        customer.setId(2);
        assertEquals(2, customer.getId());
    }

    @Test
    public void testSetLoyaltyCardId() {
        customer.setCustomerCard("12343213");
        assertEquals("12343213", customer.getCustomerCard());
    }

    @Test
    public void testSetCustomerName(){
        customer.setCustomerName("Rispondi");
        assertEquals("Rispondi", customer.getCustomerName());
    }

    @Test
    public void testSetPoints(){
        customer.setPoints(360);
        assertEquals(360, customer.getPoints());
    }

}