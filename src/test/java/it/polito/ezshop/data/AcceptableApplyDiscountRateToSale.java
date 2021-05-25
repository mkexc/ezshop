package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.InvalidDiscountRateException;
import it.polito.ezshop.exceptions.UnauthorizedException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptableApplyDiscountRateToSale {
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
        assertThrows(UnauthorizedException.class, () -> shop.applyDiscountRateToSale(1,10.0));
        //shop.endSaleTransaction(id);

        // user without privilege
        //shop.login("Carlo","abcd");
        //assertThrows(UnauthorizedException.class, () -> shop.applyDiscountRateToSale(1,10.0));
    }

    @Test
    public void invalidTransactionId() throws Exception {
        //Integer id = shop.startSaleTransaction();
        // transactionid 0
        assertThrows(InvalidTransactionIdException.class, () -> shop.applyDiscountRateToSale(0,10.0));
        // transactionid <0
        assertThrows(InvalidTransactionIdException.class, () -> shop.applyDiscountRateToSale(-1,10.0));
        // transactionid null
        assertThrows(InvalidTransactionIdException.class, () -> shop.applyDiscountRateToSale(null,10.0));
        //shop.endSaleTransaction(id);
    }

    @Test
    public void invalidDiscountRateException() throws Exception {
        Integer id = shop.startSaleTransaction();

        // discountrate<0
        assertThrows(InvalidDiscountRateException.class, () -> shop.applyDiscountRateToSale(id,-1.0));
        // 0<discountrate<1
        assertThrows(InvalidDiscountRateException.class, () -> shop.applyDiscountRateToSale(id,0.5));
        // discountrate=1
        assertThrows(InvalidDiscountRateException.class, () -> shop.applyDiscountRateToSale(id,1));

        shop.endSaleTransaction(id);
    }

    @Test
    public void nonExistingTransaction() throws Exception {
        assertFalse(shop.applyDiscountRateToSale(9999,10.0));
    }

    @Test
    public void correctCase() throws Exception {
        Integer id = shop.startSaleTransaction();
        assertTrue(shop.applyDiscountRateToSale(id,10.0));
        shop.endSaleTransaction(id);
        //shop.logout();
        //shop.login("admin","ciao");
        //Integer productId = shop.getProductTypeByBarCode("2424242424239").getId();
        //shop.updateQuantity(productId,20);
    }
}
