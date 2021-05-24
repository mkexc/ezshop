package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidPaymentException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptableReceiveCashPayment {

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
                shop.receiveCashPayment(idSaleTransaction, 100));
        shop.login("admin","ciao");
    }

    @Test
    public void testIdCorrect() {
        assertThrows(InvalidTransactionIdException.class, () ->
                shop.receiveCashPayment(null,100.0));

        assertThrows(InvalidTransactionIdException.class, () ->
                shop.receiveCashPayment(0,100.0));

        assertThrows(InvalidTransactionIdException.class, () ->
                shop.receiveCashPayment(-1,100.0));
    }

    @Test
    public void testInvalidPayment(){
        assertThrows(InvalidPaymentException.class, () ->
                shop.receiveCashPayment(idSaleTransaction,-100.0));
        assertThrows(InvalidPaymentException.class, () ->
                shop.receiveCashPayment(idSaleTransaction,0));
    }

    @Test
    public void testSaleDoesNotExist() throws Exception{
       assertTrue(shop.receiveCashPayment(999,100.0)<0);
    }

    @Test
    public void testNoMoney() throws Exception{
        assertTrue(shop.receiveCashPayment(idSaleTransaction,0.001)<0);
    }

    @Test
    public void testCorrectCase() throws Exception{
        assertTrue(shop.receiveCashPayment(idSaleTransaction,10.0)==4);
    }

    @After
    public void after() throws Exception{
        //shop.reset();
    }


}
