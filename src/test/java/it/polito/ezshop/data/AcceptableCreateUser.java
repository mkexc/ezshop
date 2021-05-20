package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;


public class AcceptableCreateUser {

    @Test
    public void testUsername(){
        EZShop shop = new EZShop();
        assertThrows(InvalidUsernameException.class, ()->
            shop.createUser("", "125465", "Cashier")
        );
        assertThrows(InvalidUsernameException.class, ()->
            shop.createUser(null, "password", "Cashier")
        );
        //shop.close();

    }
    @Test
    public void testPassword(){
        EZShop shop = new EZShop();
        assertThrows(InvalidPasswordException.class, ()->
            shop.createUser("Carlo", "", "Cashier")
        );
        assertThrows(InvalidPasswordException.class, ()->
            shop.createUser("Carlo", null, "Cashier")
        );
        //shop.close();

    }

    @Test
    public void testRole(){
        EZShop shop = new EZShop();
        assertThrows(InvalidRoleException.class, ()->
            shop.createUser("Carlo", "1234", "")
        );
        assertThrows(InvalidRoleException.class, ()->
            shop.createUser("Carlo", "1234", null)
        );
        assertThrows(InvalidRoleException.class, ()->
            shop.createUser("Carlo", "1234", "asd")
        );
        //shop.close();

    }

    @Test
    public void testDuplicateUsername() throws Exception{
        EZShop shop = new EZShop();
            try {
                shop.createUser("Carlo", "abCd", "Cashier");

            }catch(Exception ignored){

            }
        assertEquals(-1, shop.createUser("Carlo", "sadF", "Cashier"));
        //shop.close();

    }

    @Test
    public void testCorrectCase() throws Exception{
        EZShop shop = new EZShop();

        assertInstanceOf(Integer.class, shop.createUser("Carlo", "sadF", "Cashier"));
        //shop.close();
    }

}
