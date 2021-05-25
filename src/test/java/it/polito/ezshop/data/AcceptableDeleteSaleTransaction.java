package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptableDeleteSaleTransaction {
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
        assertThrows(UnauthorizedException.class, () -> shop.deleteSaleTransaction(1));
        //shop.endSaleTransaction(id);
    }

    @Test
    public void invalidTransactionId() throws Exception {
        //Integer id = shop.startSaleTransaction();
        // transactionid 0
        assertThrows(InvalidTransactionIdException.class, () -> shop.deleteSaleTransaction(0));
        // transactionid <0
        assertThrows(InvalidTransactionIdException.class, () -> shop.deleteSaleTransaction(-1));
        // transactionid null
        assertThrows(InvalidTransactionIdException.class, () -> shop.deleteSaleTransaction(null));
        //shop.endSaleTransaction(id);
    }

    @Test
    public void nonExistingTransaction() throws Exception {
        assertFalse(shop.deleteSaleTransaction(9999));
    }

    @Test
    public void transactionAlreadyPayed() throws Exception {
        Integer id = shop.startSaleTransaction();
        shop.addProductToSale(id,"2424242424239",1);
        shop.receiveCashPayment(id, 2);

        // TODO sistemare receivecashpayement?
        assertFalse(shop.deleteSaleTransaction(id));
    }

    @Test
    public void dbProblem() throws Exception {
        //TODO testing db problem in deletesaletransaction
    }

    @Test
    public void correctCase() throws Exception {
        Integer id = shop.startSaleTransaction();
        assertTrue(shop.deleteSaleTransaction(id));
    }
}
