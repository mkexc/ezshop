package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidCreditCardException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class AcceptableReceiveCreditCardPayment {
    private it.polito.ezshop.data.EZShop shop;
    private int idSaleTransaction;
    @Before
    public void before() throws Exception{
        shop = new it.polito.ezshop.data.EZShop();
        shop.login("admin","ciao");
        idSaleTransaction = shop.startSaleTransaction();
        shop.addProductToSale(idSaleTransaction,"2424242424239",3);
        shop.endSaleTransaction(idSaleTransaction);
    }

    @Test
    public void authTest() throws Exception {
        shop.logout();
        assertThrows(UnauthorizedException.class, () ->
                shop.receiveCreditCardPayment(idSaleTransaction, "4024007103682604"));
        shop.login("admin","ciao");
    }

    @Test
    public void testIdCorrect() {
        assertThrows(InvalidTransactionIdException.class, () ->
                shop.receiveCreditCardPayment(null,"4024007103682604"));

        assertThrows(InvalidTransactionIdException.class, () ->
                shop.receiveCreditCardPayment(0,"4024007103682604"));

        assertThrows(InvalidTransactionIdException.class, () ->
                shop.receiveCreditCardPayment(-1,"4024007103682604"));
    }

    @Test
    public void testCreditCard(){
        assertThrows(InvalidCreditCardException.class, () ->
                shop.receiveCreditCardPayment(idSaleTransaction,""));
        assertThrows(InvalidCreditCardException.class, () ->
                shop.receiveCreditCardPayment(idSaleTransaction,null));
        assertThrows(InvalidCreditCardException.class, () ->
                shop.receiveCreditCardPayment(idSaleTransaction,"4024007103682604"));
    }

    @Test
    public void testNoSaleTransactionId() throws Exception{
        assertFalse(shop.receiveCreditCardPayment(9999, "4556737586899855"));
    }

    @Test
    public void cardNotExist() throws Exception{
        assertFalse(shop.receiveCreditCardPayment(idSaleTransaction,"3531955240966819"));
    }

    @Test
    public void testNoMoney() throws Exception{
        assertFalse(shop.receiveCreditCardPayment(idSaleTransaction,"4556737586899855"));
    }

    @Test
    public void testCorrectCase() throws Exception{
        assertTrue(shop.receiveCreditCardPayment(idSaleTransaction,"3531955240966819"));
    }

    @After
    public void after() throws Exception{
        //shop.reset();
    }
}
