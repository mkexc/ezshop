package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AcceptableGetAllOrders {

    private it.polito.ezshop.data.EZShop shop;

    @BeforeEach
    public void before() throws Exception {
        shop = new it.polito.ezshop.data.EZShop();
        shop.login("admin","ciao");
    }

    @AfterEach
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
