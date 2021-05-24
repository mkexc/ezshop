package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptableStartReturnTransaction {
    private it.polito.ezshop.data.EZShop shop;
    private int idSaleTransaction;


    @Before
    public void before() throws Exception{
        shop = new it.polito.ezshop.data.EZShop();
        shop.login("admin","ciao");
        idSaleTransaction = shop.startSaleTransaction();
    }

    @Test
    public void authTest() throws Exception {
        shop.logout();
        assertThrows(UnauthorizedException.class, () ->
                shop.startReturnTransaction(idSaleTransaction));

        shop.login("admin","ciao");
    }

    @Test
    public void testIdCorrect() {
        assertThrows(InvalidTransactionIdException.class, () ->
                shop.startReturnTransaction(null));

        assertThrows(InvalidTransactionIdException.class, () ->
                shop.startReturnTransaction(0));

        assertThrows(InvalidTransactionIdException.class, () ->
                shop.startReturnTransaction(-1));
    }
    @Test
    public void testNoTransactionPresent() throws Exception{
        assertEquals(-1,shop.startReturnTransaction(999).intValue());
    }

    @Test
    public void testCorrectCase() throws Exception{
        int idReturnTransaction= shop.startReturnTransaction(idSaleTransaction);
        assertTrue( idReturnTransaction>0);
        shop.deleteReturnTransaction(idReturnTransaction);

    }

    @After
    public void after() throws Exception {
        shop.deleteSaleTransaction(idSaleTransaction);
        shop.logout();
    }

}
