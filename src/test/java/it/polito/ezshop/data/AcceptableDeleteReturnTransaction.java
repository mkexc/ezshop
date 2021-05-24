package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptableDeleteReturnTransaction {

    private it.polito.ezshop.data.EZShop shop;
    private int idSaleTransaction;
    private int idReturnTransaction;

    @Before
    public void before() throws Exception{
        shop = new it.polito.ezshop.data.EZShop();
        shop.login("admin","ciao");
        idSaleTransaction = shop.startSaleTransaction();
        idReturnTransaction= shop.startReturnTransaction(idSaleTransaction);
        shop.addProductToSale(idSaleTransaction,"2424242424239",3);
        // adding the product to return
        shop.returnProduct(idReturnTransaction,"2424242424239",2);

    }

    @Test
    public void authTest() throws Exception {
        shop.logout();
        assertThrows(UnauthorizedException.class, () ->
                shop.deleteReturnTransaction(idReturnTransaction));

        shop.login("admin","ciao");
    }

    @Test
    public void testIdCorrect() {
        assertThrows(InvalidTransactionIdException.class, () ->
                shop.deleteReturnTransaction(null));

        assertThrows(InvalidTransactionIdException.class, () ->
                shop.deleteReturnTransaction(0));

        assertThrows(InvalidTransactionIdException.class, () ->
                shop.deleteReturnTransaction(-1));
    }

    @Test
    public void testNoReturnTransactionId() throws Exception{
        assertFalse(shop.deleteReturnTransaction(9999));
    }

    @Test
    public void testCorrectCase() throws Exception{
        assertTrue(shop.deleteReturnTransaction(idReturnTransaction));
        // resetting the status
        idReturnTransaction= shop.startReturnTransaction(idSaleTransaction);
        shop.addProductToSale(idSaleTransaction,"2424242424239",3);
        // adding the product to return
        shop.returnProduct(idReturnTransaction,"2424242424239",2);

    }

    @Test
    public void testPayed() throws Exception{
        shop.endReturnTransaction(idReturnTransaction,true);
        shop.returnCashPayment(idReturnTransaction);
        assertFalse(shop.deleteReturnTransaction(idReturnTransaction));
    }

    @After
    public void after() throws Exception{
        //shop.reset();
    }

}
