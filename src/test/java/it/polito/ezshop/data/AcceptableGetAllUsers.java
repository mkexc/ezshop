package it.polito.ezshop.data;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;



public class AcceptableGetAllUsers {

    @Test
    public void testAuthorization() throws Exception {


        EZShop shop = new EZShop();
        shop.login("23", "12345");
        assertThrows(UnauthorizedException.class, shop::getAllUsers
        );
        //shop.close();
    }
        @Test
        public void testCorrect() throws Exception {
            EZShop shop = new EZShop();
            shop.login("admin","ciao");
            assertTrue(shop.getAllUsers() instanceof ArrayList);
          //  shop.close();
         }
    }

