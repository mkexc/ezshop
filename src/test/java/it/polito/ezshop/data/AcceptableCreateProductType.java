package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

public class AcceptableCreateProductType {

    private EZShop shop;
    private Integer toDeleteID=0;

    @Nested
    class loginAndLogoutForEach{
        @BeforeEach
        public void beforeEach()throws Exception{
            shop = new EZShop();
            shop.login("admin","ciao");
        }

        @AfterEach
        public void afterEach() {
            shop.logout();
        }

        @Test
        public void testPricePerUnit() {

            assertThrows(InvalidPricePerUnitException.class,()-> shop.createProductType("Latte", "00012345678905", -13.3, "Vecchio"));

            assertThrows(InvalidPricePerUnitException.class,()-> shop.createProductType("Biscotti", "00012345678905", 0.0, "Vecchio"));
        }

        @Test
        public void testDescription() {
            assertThrows(InvalidProductDescriptionException.class,()-> shop.createProductType("", "00012345678905", 13.3, "Vecchio"));

            assertThrows(InvalidProductDescriptionException.class,()-> shop.createProductType(null, "00012345678905", 0.1, "Vecchio"));
        }

        @Test
        public void testProductCode() throws Exception{
            assertThrows(InvalidProductCodeException.class,()-> shop.createProductType("Latte", "", 13.3, "Vecchio"));

            assertThrows(InvalidProductCodeException.class,()-> shop.createProductType("Latte", null, 0.1, "Vecchio"));

            assertThrows(InvalidProductCodeException.class,()-> shop.createProductType("Latte", "12345678910", 0.1, "Vecchio"));

            // SAME BARCODE PRESENT
            assertEquals(-1,shop.createProductType("Uova","11234567890125",3.4,"Di Struzzo"));
        }

        @Nested
        class correct{

            @AfterEach
            public void afterEach() throws Exception {
                shop.deleteProductType(toDeleteID);
            }

            @Test
            public void testCorrectCase() throws Exception{
                toDeleteID = shop.createProductType("Duriano", "12345678901286", 1.5, "better than his brother durian");
                assertTrue(toDeleteID>0);
            }

        }

        @Test
        public void testErrorCase() throws Exception{
            shop.createProductType("Durian", "1234567890128", 1.5, "It does not taste good");
            assertEquals(-1, shop.createProductType("Durian", "1234567890128", 1.5, "It does not tast good"));
        }


    }


    @Nested
    class NotLogged{
        @BeforeEach
        public void before(){
            shop = new EZShop();
        }
        @Test
        public void testAuthorization() throws Exception{
            assertThrows(UnauthorizedException.class,()->
                    shop.createProductType("Latte", "00012345678905", 13.3, "Vecchio" )
            );
            shop.login("23","12345");
            assertThrows(UnauthorizedException.class,()->
                    shop.createProductType("Latte", "00012345678905", 13.3, "Vecchio" )
            );

        }
        @AfterEach
        public void after(){
            shop.logout();
        }

    }

}




