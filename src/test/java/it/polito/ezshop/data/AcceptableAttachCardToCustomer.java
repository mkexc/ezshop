package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptableAttachCardToCustomer {
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
    public void invalidCustomerId()
    {
        assertThrows(InvalidCustomerIdException.class, () -> shop.attachCardToCustomer("0000000001",0));
        assertThrows(InvalidCustomerIdException.class, () -> shop.attachCardToCustomer("0000000001",-1));
        assertThrows(InvalidCustomerIdException.class, () -> shop.attachCardToCustomer("0000000001",null));
    }

    @Test
    public void invalidCardId()
    {
        assertThrows(InvalidCustomerCardException.class, () -> shop.attachCardToCustomer("",1));
        assertThrows(InvalidCustomerCardException.class, () -> shop.attachCardToCustomer("00000001",1));
        assertThrows(InvalidCustomerCardException.class, () -> shop.attachCardToCustomer("000000000100",1));
        assertThrows(InvalidCustomerCardException.class, () -> shop.attachCardToCustomer(null,1));
    }

    @Test
    public void noUser() throws InvalidCustomerIdException, UnauthorizedException, InvalidCustomerCardException {
        assertFalse(shop.attachCardToCustomer("0000000001",3));
    }

    @Test
    public void alreadyAssigned() throws InvalidCustomerIdException, UnauthorizedException, InvalidCustomerCardException {
        assertFalse(shop.attachCardToCustomer("0000000001",10));
    }

    @Test
    public void correctCase() throws InvalidCustomerIdException, UnauthorizedException, InvalidCustomerCardException {
        assertTrue(shop.attachCardToCustomer("0000000002",10));
        shop.detachCustomerCard("0000000002");
    }

}
