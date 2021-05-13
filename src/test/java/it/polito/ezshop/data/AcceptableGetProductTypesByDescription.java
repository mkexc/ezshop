package it.polito.ezshop.data;

import org.junit.*;
import it.polito.ezshop.exceptions.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AcceptableGetProductTypesByDescription {

    @Test
    public void testAuthorization() throws Exception {

        EZShop shop = new EZShop();
        assertThrows(UnauthorizedException.class, () ->
                shop.getProductTypesByDescription("Latte")
        );
        shop.login("23", "12345");
        assertThrows(UnauthorizedException.class, () ->
                shop.getProductTypesByDescription("Latte")
        );
    }

    @Test
    public void testValid() throws Exception
    {
        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        List<ProductType> l=shop.getProductTypesByDescription("Latte");
        assertFalse(l.isEmpty());
        shop.logout();
    }

    @Test
    public void testMultipleValid() throws Exception
    {
        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        List<ProductType> l=shop.getProductTypesByDescription("Carne");
        assertTrue(l.size()>1);
        shop.logout();
    }

    @Test
    public void testNotValid() throws Exception
    {
        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        List<ProductType> l=shop.getProductTypesByDescription("Cacca");
        assertTrue(l.isEmpty());
        shop.logout();
    }

}
