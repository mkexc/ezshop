package it.polito.ezshop.data;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class AcceptableLogout {

    private it.polito.ezshop.data.EZShop shop;


        @Test
        public void testNoLoggedUser() {
            shop= new it.polito.ezshop.data.EZShop();
            assertFalse(shop.logout());
        }

        @Test
        public void testCorrectCase() throws Exception {
            shop= new it.polito.ezshop.data.EZShop();
            shop.login("admin","ciao");
            assertTrue(shop.logout());
        }


}
