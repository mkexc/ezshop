package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AcceptableDeleteCustomer {
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
                shop.deleteCustomer(customerId));

        shop.login("admin","ciao");
    }

    @Test
    public void TestId() throws Exception {
        assertThrows(InvalidCustomerIdException.class, () ->
                shop.deleteCustomer(-3)
        );
        assertThrows(InvalidCustomerIdException.class, () ->
                shop.deleteCustomer(null)
        );
        assertFalse(shop.deleteCustomer(333));

    }

    @Test
    public void TestCorrectCase() throws Exception {
        assertTrue(shop.deleteCustomer(customerId));
    }



}
