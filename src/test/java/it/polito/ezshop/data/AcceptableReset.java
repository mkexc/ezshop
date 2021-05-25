package it.polito.ezshop.data;

import org.junit.*;

import static org.junit.Assert.*;

public class AcceptableReset {
    EZShop shop;

    @Before
    public void before() throws Exception
    {
        shop = new EZShop();
        shop.reset();
        shop.login("admin","ciao");
    }

    @Test
    public void check1() throws Exception{
        assertSame(0,shop.getCreditsAndDebits(null,null).size());
    }

    @Test
    public void check2() throws Exception{
        assertSame(0,shop.getAllProductTypes().size());
    }

    @Test
    public void check3() throws Exception{
        assertSame(0,shop.getAllOrders().size());
    }

    @Test
    public void check4() throws Exception{
        assertSame(2,shop.getAllCustomers().size());
        assertSame(2,shop.getAllUsers().size());
    }

}
