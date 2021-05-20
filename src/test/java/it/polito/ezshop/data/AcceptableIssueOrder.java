package it.polito.ezshop.data;


import it.polito.ezshop.exceptions.*;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AcceptableIssueOrder {

    @Test
    public void authTest() throws Exception
    {
        EZShop shop = new EZShop();
        assertThrows(UnauthorizedException.class, () ->
                shop.issueOrder("11234567890125",5,30.0)
        );
        shop.login("23", "12345");
        assertThrows(UnauthorizedException.class, () ->
                shop.issueOrder("11234567890125",5,30.0)
        );
    }

    @Test
    public void invalidProductCode() throws Exception
    {
        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        assertThrows(InvalidProductCodeException.class, () ->
                shop.issueOrder("01010100101010",5,30.0)
        );
    }

    @Test
    public void validProductCodeNotInDB() throws Exception
    {
        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        assertEquals(-1,shop.issueOrder("652343546457",5,30.0));
    }

    @Test
    public void invalidPricePerUnit() throws Exception
    {
        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        assertThrows(InvalidPricePerUnitException.class, () ->
                shop.issueOrder("11234567890125",5,-30.0)
        );
    }

    @Test
    public void invalidQuantity() throws Exception
    {
        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        assertThrows(InvalidQuantityException.class, () ->
                shop.issueOrder("11234567890125",-5,30.0)
        );
        assertThrows(InvalidQuantityException.class, () ->
                shop.issueOrder("11234567890125",0,30.0)
        );
    }

    @Test
    public void validOrder() throws Exception
    {
        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        Integer id=shop.issueOrder("11234567890125",5,30.0);
        assertTrue(id>0);

    }
}
