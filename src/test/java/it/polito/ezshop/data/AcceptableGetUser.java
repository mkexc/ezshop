package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.OrderWith;
import org.junit.runner.manipulation.Alphanumeric;

import static org.junit.jupiter.api.Assertions.*;

@OrderWith(Alphanumeric.class)
public class AcceptableGetUser {
//    @BeforeEach
//    public void init() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
//        EZShop shop = new EZShop();
//        shop
//    }

    @Test
    public void testAuthorization() throws Exception {


        EZShop shop = new EZShop();
        shop.login("23", "12345");
        assertThrows(UnauthorizedException.class, shop::getAllUsers);
        //shop.close();
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
        //shop.close();

    }

    @Test
    public void testGetUser() throws Exception{
        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        Integer id = shop.createUser("Franco","ciaoCiao","Cashier");
        assertEquals(id, shop.getUser(id).getId());
        shop.deleteUser(id);
        //shop.close();

    }

    @Test
    public void testNotFoundUser() throws Exception{
        EZShop shop = new EZShop();
        shop.login("admin","ciao");

        assertNull(shop.getUser(999));

        //shop.close();

    }

}
