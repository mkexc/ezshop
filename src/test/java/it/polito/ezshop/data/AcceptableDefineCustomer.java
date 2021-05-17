package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AcceptableDefineCustomer {
    private it.polito.ezshop.data.EZShop shop;
    private int customerId;

    @BeforeEach
    public void before() throws Exception{
        shop = new it.polito.ezshop.data.EZShop();
        shop.login("admin","ciao");
        customerId=shop.defineCustomer("Obama");

    }

    @AfterEach
    public void after() throws Exception{
        shop.deleteCustomer(customerId);
        shop.logout();
    }

    @Test
    public void authTest() throws Exception {
        shop.logout();
        assertThrows(UnauthorizedException.class, () ->
                shop.defineCustomer("Trump")
        );
        shop.login("admin","ciao");
    }

    @Test
    public void TestCustomerName() {

        assertThrows(InvalidCustomerNameException.class, () ->
                shop.defineCustomer("")
        );

        assertThrows(InvalidCustomerNameException.class, () ->
                shop.defineCustomer(null)
        );
    }

    @Test
    public void TestCorrectCase() throws Exception {
        Integer customerId2 = shop.defineCustomer("Biden");
        assertInstanceOf(Integer.class, customerId2);
        shop.deleteCustomer(customerId2);
    }
}
