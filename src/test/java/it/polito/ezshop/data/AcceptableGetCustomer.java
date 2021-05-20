package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AcceptableGetCustomer {

    private it.polito.ezshop.data.EZShop shop;
    private int customerId;

    @BeforeEach
    public void before() throws Exception{
        shop = new it.polito.ezshop.data.EZShop();
        shop.login("admin","ciao");
        customerId=shop.defineCustomer("Obama");

    }

    @AfterEach
    public void after() throws Exception {
        shop.deleteCustomer(customerId);
        shop.logout();
    }

    @Test
    public void authTest() throws Exception {
        shop.logout();
        assertThrows(UnauthorizedException.class, () ->
                shop.getCustomer(customerId));

        shop.login("admin","ciao");
    }

    @Test
    public void TestId() throws Exception {
        assertThrows(InvalidCustomerIdException.class, () ->
                shop.getCustomer(-3)
        );
        assertThrows(InvalidCustomerIdException.class, () ->
                shop.getCustomer(null)
        );
        assertNull(shop.getCustomer(333));

    }

    @Test
    public void TestCorrectCase() throws Exception {
        assertEquals("Obama",shop.getCustomer(customerId).getCustomerName());
    }
}
