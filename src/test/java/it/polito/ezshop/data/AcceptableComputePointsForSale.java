package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptableComputePointsForSale {
    EZShop shop;

    @Before
    public void beforeEach() throws Exception {
        shop = new EZShop();
        shop.login("23","12345");
    }

    @After
    public void afterEach() {
        shop.logout();
    }

    @Test
    public void authTest() throws Exception {
        // no logged user
        shop.logout();
        //Integer id = shop.startSaleTransaction();
        assertThrows(UnauthorizedException.class, () -> shop.computePointsForSale(1));
        //shop.endSaleTransaction(id);
    }

    @Test
    public void invalidTransactionId() throws Exception {
        //Integer id = shop.startSaleTransaction();
        // transactionid 0
        assertThrows(InvalidTransactionIdException.class, () -> shop.computePointsForSale(0));
        // transactionid <0
        assertThrows(InvalidTransactionIdException.class, () -> shop.computePointsForSale(-1));
        // transactionid null
        assertThrows(InvalidTransactionIdException.class, () -> shop.computePointsForSale(null));
        //shop.endSaleTransaction(id);
    }

    @Test
    public void nonExistingTransaction() throws Exception {
        assertSame(-1, shop.computePointsForSale(9999));
    }

    @Test
    public void correctCase() throws Exception {
        Integer id = shop.startSaleTransaction();
        shop.addProductToSale(id, "2424242424239", 5);
        // TODO sistemare computepointsforsale
        assertSame(1, shop.computePointsForSale(id));
        shop.endSaleTransaction(id);
    }
}
