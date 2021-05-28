package it.polito.ezshop.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;



public class AcceptableCreditCard {
    private it.polito.ezshop.model.CreditCard card;

    @Before
    public void newCustomer(){
        card = new it.polito.ezshop.model.CreditCard("4916560768986372",  370);
    }

    @Test
    public void testGetCardNumber() {
        assertEquals("4916560768986372", card.getCardNumber());
    }

    @Test
    public void testGetBalance() {
        assertEquals(370, card.getBalance(), 0);
    }
    
    @Test
    public void testSetCardNumber() {
        card.setCardNumber("4916168367711421");
        assertEquals("4916168367711421", card.getCardNumber());
    }

    @Test
    public void testSetBalance() {
        card.setBalance(333);
        assertEquals(333, card.getBalance(),0);
    }

    @Test
    public void testValidateWithLuhn() {
        assertTrue(it.polito.ezshop.model.CreditCard.validateWithLuhn("4485370086510891"));
        assertTrue(it.polito.ezshop.model.CreditCard.validateWithLuhn("5100293991053009"));
        assertTrue(it.polito.ezshop.model.CreditCard.validateWithLuhn("4716258050958645"));
        assertFalse(it.polito.ezshop.model.CreditCard.validateWithLuhn("4324332424"));
    }

}
,