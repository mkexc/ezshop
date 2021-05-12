package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AcceptableDeleteUser {

    @Test
    public void deleteUsernameNotAuthorized() throws InvalidPasswordException, InvalidUsernameException {
        EZShop shop = new EZShop();
        shop.login("23","12345"); //not an Admin
        assertThrows(UnauthorizedException.class, ()->
            shop.deleteUser(1)
        );
        shop.logout();
        shop.close();
    }

    @Test
    public void deleteUsernameNotPresent() throws InvalidUserIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        assertFalse(shop.deleteUser(3));
        shop.logout();
        shop.close();
    }

    @Test
    public void userNotLogged() {
        EZShop shop = new EZShop();
        assertThrows(UnauthorizedException.class, ()->
            shop.deleteUser(1)
        );
        shop.close();
    }

    @Test
    public void invalidUserId() throws InvalidPasswordException, InvalidUsernameException {
        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        assertThrows(InvalidUserIdException.class, ()->
            shop.deleteUser(-1)
        );
        shop.logout();
        shop.close();
    }

    @Test
    public void userDeletable() throws InvalidPasswordException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException, InvalidRoleException {
        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        assertTrue(shop.deleteUser(shop.createUser("temp","temp","Administrator")));
        shop.logout();
        shop.close();
    }





}
