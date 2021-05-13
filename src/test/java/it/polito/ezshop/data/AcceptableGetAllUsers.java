package it.polito.ezshop.data;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;



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
            assertInstanceOf(java.util.ArrayList.class, shop.getAllUsers());
          //  shop.close();
         }
    }

