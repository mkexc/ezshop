package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptableReturnProduct {

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
    }

    @Test
    public void authTest() throws Exception {
        shop.logout();
        assertThrows(UnauthorizedException.class, () ->
                shop.returnProduct(idReturnTransaction,"",1));

        shop.login("admin","ciao");
    }

    @Test
    public void testInvalidProductCode() {
        assertThrows(InvalidProductCodeException.class, ()-> shop.returnProduct(idReturnTransaction, null, 1));
        assertThrows(InvalidProductCodeException.class,()-> shop.returnProduct(idReturnTransaction,"",1));
        assertThrows(InvalidProductCodeException.class,()-> shop.returnProduct(idReturnTransaction,"11",1));
    }

    @Test
    public void testInvalidQuantity(){
        assertThrows(InvalidQuantityException.class, ()-> shop.returnProduct(idReturnTransaction, "2424242424239", 0));
        assertThrows(InvalidQuantityException.class,()-> shop.returnProduct(idReturnTransaction,"2424242424239",-1));
    }

    @Test
    public void testIdCorrect() {
        assertThrows(InvalidTransactionIdException.class, () ->
                shop.returnProduct(null,"2424242424239",1));

        assertThrows(InvalidTransactionIdException.class, () ->
                shop.returnProduct(0,"2424242424239",1));

        assertThrows(InvalidTransactionIdException.class, () ->
                shop.returnProduct(-1,"2424242424239",1));
    }

    @Test
    public void testNoProductInSaleTransaction() throws Exception {
        assertFalse(shop.returnProduct(idReturnTransaction,"2143325343648",1));
    }

    @Test
    public void testNoProductInProductType() throws Exception{
        assertFalse(shop.returnProduct(idReturnTransaction,"21421342351248",1));
    }

    @Test
    public void testTooHighQuantity() throws Exception{
        assertFalse(shop.returnProduct(idReturnTransaction,"2424242424239",4));
    }

    @Test
    public void testNoReturnTransactionId() throws Exception{
        assertFalse(shop.returnProduct(999,"2424242424239",1));
    }

    @Test
    public void testCorrectCase() throws Exception{
        assertTrue(shop.returnProduct(idReturnTransaction,"2424242424239",2));
    }


    @After
    public void after() throws Exception {
        shop.deleteSaleTransaction(idSaleTransaction);
        shop.deleteReturnTransaction(idReturnTransaction);
        shop.logout();
    }

}
