package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AcceptablePayOrder {

    private it.polito.ezshop.data.EZShop shop;
    private int orderIdPayed,  orderIdIssued;

    @BeforeEach
    public void before() throws Exception{
        shop = new it.polito.ezshop.data.EZShop();
        shop.login("admin","ciao");
        orderIdPayed = shop.payOrderFor("2143325343648",1,1.0);
        orderIdIssued= shop.issueOrder("11234567890125",1,1.0);

    }

    @AfterEach
    public void after(){
        shop.logout();
        shop.deleteOrderId(orderIdPayed);
        shop.deleteOrderId(orderIdIssued);
    }

    @Test
    public void authTest() throws Exception {
        shop.logout();
        assertThrows(UnauthorizedException.class, () ->
                shop.payOrder(1)
        );
        shop.login("23", "12345");
        assertThrows(UnauthorizedException.class, () ->
                shop.payOrder(1)
        );
    }

    @Test
    public void TestInvalidOrderId() throws Exception {
        shop.login("admin", "ciao");
        assertFalse(shop.payOrder(300));

        assertThrows(InvalidOrderIdException.class, () ->
                shop.payOrder(-300)
        );
        assertThrows(InvalidOrderIdException.class, () ->
                shop.payOrder(null)
        );
    }

    @Test
    public void TestJustPayed() throws Exception {
        shop.login("admin", "ciao");
        assertFalse(shop.payOrder(orderIdPayed));
    }

    @Test
    public void TestCorrectCase() throws Exception {

        assertTrue(shop.payOrder(orderIdIssued));
    }

}