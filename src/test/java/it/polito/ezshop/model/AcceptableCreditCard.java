package it.polito.ezshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AcceptableCreditCard {
    private it.polito.ezshop.model.CreditCard card;

    @BeforeEach
    public void newCustomer(){
        card = new it.polito.ezshop.model.CreditCard("4916560768986372",  370);
    }

    @Test
    public void testGetCardNumber() {
        assertEquals("4916560768986372", card.getCardNumber());
    }

    @Test
    public void testGetBalance() {
        assertEquals(370, card.getBalance());
    }
    
    @Test
    public void testSetCardNumber() {
        card.setCardNumber("4916168367711421");
        assertEquals("4916168367711421", card.getCardNumber());
    }

    @Test
    public void testSetBalance() {
        card.setBalance(333);
        assertEquals(333, card.getBalance());
    }

    @Test
    public void testValidateWithLuhn() {

        assertTrue(it.polito.ezshop.model.CreditCard.validateWithLuhn("4556737586899855"));
        assertFalse(it.polito.ezshop.model.CreditCard.validateWithLuhn("4324332424"));
    }
}
