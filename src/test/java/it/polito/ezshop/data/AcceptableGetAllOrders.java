package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class AcceptableGetAllOrders {

    private it.polito.ezshop.data.EZShop shop;

    @Before
    public void before() throws Exception {
        shop = new it.polito.ezshop.data.EZShop();
        shop.login("admin","ciao");
    }

    @After
    public void after(){
        shop.logout();
    }

    @Test
    public void authTest() throws Exception {
        shop.logout();
        assertThrows(UnauthorizedException.class, () ->
                shop.getAllOrders()
        );
        shop.login("23", "12345");
        assertThrows(UnauthorizedException.class, () ->
                shop.getAllUsers()
        );
    }

    @Test
    public void correctCase() throws Exception {
        List<Order> list= shop.getAllOrders();
        assertTrue(!list.isEmpty());
    }


}
