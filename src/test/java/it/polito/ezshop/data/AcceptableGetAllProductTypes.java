package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AcceptableGetAllProductTypes {

    private it.polito.ezshop.data.EZShop shop;
    @Test
    public void testAuthorization() throws Exception {
        shop = new it.polito.ezshop.data.EZShop();
        assertThrows(UnauthorizedException.class, shop::getAllUsers);
    }

    @Test
    public void testCorrectCase() throws Exception {
        shop = new it.polito.ezshop.data.EZShop();
        shop.login("admin","ciao");
        assertInstanceOf(ArrayList.class, shop.getAllProductTypes());
    }


}
