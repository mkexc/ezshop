package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptableStartSaleTransaction {
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
        assertThrows(UnauthorizedException.class, () -> shop.startSaleTransaction());
        shop.login("23","12345");
    }

    @Test
    public void correctCase() throws Exception
    {
        Integer id = shop.startSaleTransaction();
        assertTrue(id.intValue()>=0);
        shop.deleteSaleTransaction(id);
    }

}
