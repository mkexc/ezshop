package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptableRecordBalanceUpdate {
    private it.polito.ezshop.data.EZShop shop;

    @Before
    public void before() throws Exception{
        shop = new it.polito.ezshop.data.EZShop();
        shop.login("admin","ciao");
    }

    @Test
    public void authTest() throws Exception {
        shop.logout();
        assertThrows(UnauthorizedException.class, () ->
                shop.recordBalanceUpdate(100));
        shop.login("23","12345");
        assertThrows(UnauthorizedException.class, () ->
                shop.recordBalanceUpdate(100));
        shop.logout();
        shop.login("admin","ciao");
    }

    @Test
    public void testNegativeBalance() throws Exception{
        assertFalse(shop.recordBalanceUpdate(-10000000));
    }

    @Test
    public void testCorrectCase() throws Exception{
        assertTrue(shop.recordBalanceUpdate(100));
        assertTrue(shop.recordBalanceUpdate(-100));
    }

    @After
    public void after(){
        // shop.reset();
    }
}
