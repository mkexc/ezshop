package it.polito.ezshop.data;

import org.junit.*;
import it.polito.ezshop.exceptions.*;

import java.util.List;

import static org.junit.Assert.*;

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
        Integer id1 = shop.createProductType("Latte","23452427445635",23.0,"Parzialmente scremato");
        Integer id2 = shop.createProductType("Latte","1231344234229",23.0,"Parzialmente scremato 2");
        List<ProductType> l=shop.getProductTypesByDescription("Latte");
        assertFalse(l.isEmpty());
        shop.deleteProductType(id1);
        shop.deleteProductType(id2);
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
