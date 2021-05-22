package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import org.junit.Test;
//import org.junit.TestFactory;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

public class AcceptableLogin {
    @Test
    public void testUsername(){
        EZShop shop = new EZShop();
        assertThrows(InvalidUsernameException.class, ()->
                shop.login("", "125465")
        );
        assertThrows(InvalidUsernameException.class, ()->
                shop.login(null, "password")
        );
        //shop.close();

    }
    @Test
    public void testPassword(){
        EZShop shop = new EZShop();
        assertThrows(InvalidPasswordException.class, ()->
                shop.login("Carlo", "")
        );
        assertThrows(InvalidPasswordException.class, ()->
                shop.login("Carlo", null)
        );
        //shop.close();

    }

   @Test
   public void testTwoLoggedUser() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        assertEquals(null,shop.login("23","1234"));

        //shop.close();

    }
}
