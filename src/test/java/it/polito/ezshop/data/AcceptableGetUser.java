package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AcceptableGetUser {
    @Test
    public void testAuthorization() throws Exception {


        EZShop shop = new EZShop();
        shop.login("23", "12345");
        assertThrows(UnauthorizedException.class, shop::getAllUsers);
        shop.close();
    }
    @Test
    public void testCorrectId() throws Exception{
        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        assertThrows(InvalidUserIdException.class, ()->
           shop.getUser(-4)
        );
        assertThrows(InvalidUserIdException.class, ()->
            shop.getUser(null)
        );
        shop.close();

    }

    @Test
    public void testGottenUser() throws Exception{
        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        Integer id = shop.createUser("Franco","ciaoCiao","Cashier");
        assertEquals(id, shop.getUser(id).getId());

        shop.close();

    }

    @Test
    public void testNotFoundUser() throws Exception{
        EZShop shop = new EZShop();
        shop.login("admin","ciao");

        assertNull(shop.getUser(999));

        shop.close();

    }

}
