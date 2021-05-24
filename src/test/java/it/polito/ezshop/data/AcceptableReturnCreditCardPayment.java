package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidCreditCardException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptableReturnCreditCardPayment {
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
        shop.endReturnTransaction(idReturnTransaction,true);
    }

    @Test
    public void authTest() throws Exception {
        shop.logout();
        assertThrows(UnauthorizedException.class, () ->
                shop.returnCreditCardPayment(idReturnTransaction,""));

        shop.login("admin","ciao");
    }

    @Test
    public void testIdCorrect() {
        assertThrows(InvalidTransactionIdException.class, () ->
                shop.returnCreditCardPayment(null,""));

        assertThrows(InvalidTransactionIdException.class, () ->
                shop.returnCreditCardPayment(0,""));

        assertThrows(InvalidTransactionIdException.class, () ->
                shop.returnCreditCardPayment(-1,""));
    }

    @Test
    public void testNoReturnTransactionId() throws Exception{
        assertEquals(-1.0,shop.returnCreditCardPayment(9999,"45567375868998550"),0.1);
    }

    @Test
    public void testNoEnded() throws Exception{
        int idReturnTransaction2= shop.startReturnTransaction(idSaleTransaction);
        // adding the product to return
        shop.returnProduct(idReturnTransaction2,"2424242424239",2);
        shop.returnCreditCardPayment(idReturnTransaction2,"45567375868998550");
    }

    @Test
    public void testCreditCard(){
        assertThrows(InvalidCreditCardException.class, () ->
                shop.returnCreditCardPayment(idReturnTransaction,""));
        assertThrows(InvalidCreditCardException.class, () ->
                shop.returnCreditCardPayment(idReturnTransaction,null));
        assertThrows(InvalidCreditCardException.class, () ->
                shop.returnCreditCardPayment(idReturnTransaction,"4024007103682604"));
    }

    @Test
    public void testNoSaleTransactionId() throws Exception{
        assertEquals(-1.0, shop.returnCreditCardPayment(9999, "45567375868998550"), 0.01);
    }

    @Test
    public void cardNotExist() throws Exception{
        assertEquals(-1.0, shop.returnCreditCardPayment(idReturnTransaction,"3531955240966819"), 0.01);
    }

    @Test
    public void testCorrectCase() throws Exception{
        assertEquals(4.0,shop.returnCreditCardPayment(idReturnTransaction,"45567375868998550"),0.01);
    }

    @After
    public void after(){
        //shop.reset();
    }
}
