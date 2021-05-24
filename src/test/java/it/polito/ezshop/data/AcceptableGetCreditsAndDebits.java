package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class AcceptableGetCreditsAndDebits {
    private it.polito.ezshop.data.EZShop shop;


    @Before
    public void before() throws Exception{
        shop = new it.polito.ezshop.data.EZShop();
        shop.login("admin","ciao");

    }

    @Test
    public void authTest() throws Exception {
        shop.logout();
        assertThrows(UnauthorizedException.class, () ->
                shop.recordBalanceUpdate(100));
        shop.login("23","12345");
        assertThrows(UnauthorizedException.class, () ->
                shop.recordBalanceUpdate(100));
        shop.logout();
        shop.login("admin","ciao");
    }

    @Test
    public void testCorrectCase() throws Exception{
        LocalDate today = LocalDate.of(2021,5,24);
        shop.recordBalanceUpdate(100);
        //assertTrue(shop.getCreditsAndDebits(null, null).size()>=1);
        assertEquals(0,shop.getCreditsAndDebits(today.minusDays(1),today).size());
        assertTrue(shop.getCreditsAndDebits(null, today).size()>0);
        assertEquals(0,shop.getCreditsAndDebits(today, null).size());
    }


}
