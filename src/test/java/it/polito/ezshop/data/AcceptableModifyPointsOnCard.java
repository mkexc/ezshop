package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptableModifyPointsOnCard {
    EZShop shop;

    @Before
    public void beforeEach() throws Exception
    {
        shop = new EZShop();
        shop.login("23","12345");
    }

    @After
    public void afterEach()
    {
        shop.logout();
    }

    @Test
    public void authTest() throws Exception
    {
        shop.logout();
        assertThrows(UnauthorizedException.class, () -> shop.attachCardToCustomer("0000000001",1));
        shop.login("23","12345");
    }

    @Test
    public void invalidCardId()
    {
        assertThrows(InvalidCustomerCardException.class, () -> shop.modifyPointsOnCard("",1));
        assertThrows(InvalidCustomerCardException.class, () -> shop.modifyPointsOnCard("00000001",1));
        assertThrows(InvalidCustomerCardException.class, () -> shop.modifyPointsOnCard("000000000100",1));
        assertThrows(InvalidCustomerCardException.class, () -> shop.modifyPointsOnCard(null,1));
    }
    @Test
    public void cardIdNotPresent() throws Exception
    {
        assertFalse(shop.modifyPointsOnCard("0001000000",1));
    }

    @Test
    public void notEnoughPoints() throws Exception
    {
        assertFalse(shop.modifyPointsOnCard("0000000001",-1));
    }

    @Test
    public void correctCase() throws Exception
    {
        assertTrue(shop.modifyPointsOnCard("0000000002",100));
        shop.modifyPointsOnCard("0000000002",-100);
    }
}
