package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidDiscountRateException;
import it.polito.ezshop.exceptions.UnauthorizedException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptableApplyDiscountRateToProduct {
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
        assertThrows(UnauthorizedException.class, () -> shop.applyDiscountRateToProduct(1,"2424242424239",10.0));
        //shop.endSaleTransaction(id);

        // user without privilege
        //shop.login("Carlo","abcd");
        //assertThrows(UnauthorizedException.class, () -> shop.applyDiscountRateToProduct(1,"278732878273",10.0));
    }

    @Test
    public void invalidTransactionId() throws Exception {
        //Integer id = shop.startSaleTransaction();
        // transactionid 0
        assertThrows(InvalidTransactionIdException.class, () -> shop.applyDiscountRateToProduct(0,"2424242424239",10.0));
        // transactionid <0
        assertThrows(InvalidTransactionIdException.class, () -> shop.applyDiscountRateToProduct(-1,"2424242424239",10.0));
        // transactionid null
        assertThrows(InvalidTransactionIdException.class, () -> shop.applyDiscountRateToProduct(null,"2424242424239",10.0));
        //shop.endSaleTransaction(id);
    }

    @Test
    public void invalidProductCodeException() throws Exception {
        Integer id = shop.startSaleTransaction();
        // productcode invalid
        assertThrows(InvalidProductCodeException.class, () -> shop.applyDiscountRateToProduct(id,"278732878273",10.0));
        assertThrows(InvalidProductCodeException.class, () -> shop.applyDiscountRateToProduct(id,"2121",10.0));
        assertThrows(InvalidProductCodeException.class, () -> shop.applyDiscountRateToProduct(id,"27873287827362737",10.0));
        // productcode empty
        assertThrows(InvalidProductCodeException.class, () -> shop.applyDiscountRateToProduct(id,"",10.0));
        // productcode null
        assertThrows(InvalidProductCodeException.class, () -> shop.applyDiscountRateToProduct(id,null,10.0));
        shop.endSaleTransaction(id);
    }

    @Test
    public void invalidDiscountRateException() throws Exception {
        Integer id = shop.startSaleTransaction();

        // discountrate<0
        assertThrows(InvalidDiscountRateException.class, () -> shop.applyDiscountRateToProduct(id,"2424242424239",-1.0));
        // 0<discountrate<1
        assertThrows(InvalidDiscountRateException.class, () -> shop.applyDiscountRateToProduct(id,"2424242424239",0.5));
        // discountrate=1
        assertThrows(InvalidDiscountRateException.class, () -> shop.applyDiscountRateToProduct(id,"2424242424239",1));

        shop.endSaleTransaction(id);
    }

    @Test
    public void nonExistingProductCode() throws Exception {
        Integer id = shop.startSaleTransaction();
        assertFalse(shop.applyDiscountRateToProduct(id,"3456243422340",10.0));
        shop.endSaleTransaction(id);
    }

    @Test
    public void transactionNotOpen() throws Exception {
        Integer id = shop.startSaleTransaction();
        shop.addProductToSale(id,"2424242424239",1);
        shop.endSaleTransaction(id);

        assertFalse(shop.applyDiscountRateToProduct(id,"2424242424239",10.0));
    }

    @Test
    public void correctCase() throws Exception {
        Integer id = shop.startSaleTransaction();
        shop.addProductToSale(id,"2424242424239",1);
        // TODO sistemare applyDiscountRateToProduct
        assertTrue(shop.applyDiscountRateToProduct(id,"2424242424239",10.0));
        shop.endSaleTransaction(id);
        //shop.logout();
        //shop.login("admin","ciao");
        //Integer productId = shop.getProductTypeByBarCode("2424242424239").getId();
        //shop.updateQuantity(productId,20);
    }
}
