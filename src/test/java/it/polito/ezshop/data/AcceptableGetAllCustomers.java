package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class AcceptableGetAllCustomers {

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
    public void unLoggedIn() throws Exception {
        shop.logout();
        assertThrows(UnauthorizedException.class, ()->
                shop.getAllCustomers()
        );
    }

    @Test
    public void cashierLoggedIn() throws Exception {
        assertTrue(shop.getAllCustomers() instanceof ArrayList);
    }


}
