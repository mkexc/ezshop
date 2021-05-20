package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import org.junit.*;
import static org.junit.jupiter.api.Assertions.*;

public class AcceptableUpdateUserRights {
    @Test
    public void userNotLogged ()
    {
        EZShop shop = new EZShop();
        assertThrows(UnauthorizedException.class, ()->shop.updateUserRights(1, "Cashier"));
        //shop.close();
    }


    @Test
    public void userNotAuthorized() throws InvalidPasswordException, InvalidUsernameException {
        EZShop shop = new EZShop();
        shop.login("23","12345");
        assertThrows(UnauthorizedException.class, ()->shop.updateUserRights(1, "Cashier"));
        shop.logout();
        //shop.close();
    }

    @Test
    public void invalidUserId() throws InvalidPasswordException, InvalidUsernameException {
        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        assertThrows(InvalidUserIdException.class, ()->shop.updateUserRights(0, "Cashier"));
        shop.logout();
        //shop.close();
    }

    @Test
    public void invalidRole() throws InvalidPasswordException, InvalidUsernameException {
        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        assertThrows(InvalidRoleException.class, ()->shop.updateUserRights(1, "Boh"));
        shop.logout();
        //shop.close();
    }

    @Test
    public void userNotPresent() throws InvalidPasswordException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException, InvalidRoleException {
        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        assertFalse(shop.updateUserRights(999, "Cashier"));
        shop.logout();
        //shop.close();
    }
}
