package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class AcceptableModifyCustomer {

    private EZShop shop;
    Integer id,id2;

    @Before
    public void before() throws Exception {
        shop = new EZShop();
        shop.reset();
        shop.login("23","12345");
        id = shop.defineCustomer("Giovanni");
        id2 = shop.defineCustomer("Pino");
        shop.modifyCustomer(id2,"Pino","3465446777744");
    }

    @After
    public void after() throws Exception{
        shop.deleteCustomer(id);
        shop.deleteCustomer(id2);
        shop.reset();
        shop.logout();
    }

    @Test
    public void authTest() throws Exception {
        shop.logout();
        assertThrows(UnauthorizedException.class, () ->
                shop.modifyCustomer(1,"mauro","3465446453648")
        );
        shop.login("23","12345");
    }

    @Test
    public void modifyNameOnly() throws Exception {
        assertTrue(shop.modifyCustomer(id,"Francesco",null));

    }

    @Test
    public void removeCard() throws Exception {
        assertTrue(shop.modifyCustomer(id,"Francesco",""));
    }

    @Test
    public void modifyCard() throws Exception {
        assertTrue(shop.modifyCustomer(id,"Francesco","3465446499646"));
    }

    @Test
    public void modifyCardAlreadyAssigned() throws Exception {
        assertFalse(shop.modifyCustomer(id,"Francesco","3465446777744"));
    }

    @Test
    public void invalidName() {
        assertThrows(InvalidCustomerNameException.class, () -> shop.modifyCustomer(id,"",""));
        assertThrows(InvalidCustomerNameException.class, () -> shop.modifyCustomer(id,null,""));
    }

    @Test
    public void invalidCard() {
        assertThrows(InvalidCustomerCardException.class, () -> shop.modifyCustomer(id,"Ok","156161"));
        assertThrows(InvalidCustomerCardException.class, () -> shop.modifyCustomer(id,"Ok","aaa711"));
    }

}
