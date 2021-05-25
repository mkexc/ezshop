package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptableDeleteProductFromSale {
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
        assertThrows(UnauthorizedException.class, () -> shop.deleteProductFromSale(1,"2424242424239",1));
        //shop.endSaleTransaction(id);

        // user without privilege
        //shop.login("Carlo","abcd");
        //assertThrows(UnauthorizedException.class, () -> shop.deleteProductFromSale(1,"278732878273",1));
    }

    @Test
    public void invalidTransactionId() throws Exception {
        //Integer id = shop.startSaleTransaction();
        // transactionid 0
        assertThrows(InvalidTransactionIdException.class, () -> shop.deleteProductFromSale(0,"2424242424239",3));
        // transactionid <0
        assertThrows(InvalidTransactionIdException.class, () -> shop.deleteProductFromSale(-1,"2424242424239",3));
        // transactionid null
        assertThrows(InvalidTransactionIdException.class, () -> shop.deleteProductFromSale(null,"2424242424239",3));
        //shop.endSaleTransaction(id);
    }

    @Test
    public void invalidProductCodeException() throws Exception {
        Integer id = shop.startSaleTransaction();
        // productcode invalid
        assertThrows(InvalidProductCodeException.class, () -> shop.deleteProductFromSale(id,"278732878273",3));
        assertThrows(InvalidProductCodeException.class, () -> shop.deleteProductFromSale(id,"2121",3));
        assertThrows(InvalidProductCodeException.class, () -> shop.deleteProductFromSale(id,"27873287827362737",3));
        // productcode empty
        assertThrows(InvalidProductCodeException.class, () -> shop.deleteProductFromSale(id,"",3));
        // productcode null
        assertThrows(InvalidProductCodeException.class, () -> shop.deleteProductFromSale(id,null,3));
        shop.endSaleTransaction(id);
    }

    @Test
    public void invalidQuantity() throws Exception {
        // quantity <0
        Integer id = shop.startSaleTransaction();
        assertThrows(InvalidQuantityException.class, () -> shop.deleteProductFromSale(id,"2424242424239",-1));
        shop.endSaleTransaction(id);
    }

    @Test
    public void nonExistingProductCode() throws Exception {
        Integer id = shop.startSaleTransaction();
        assertFalse(shop.deleteProductFromSale(id,"3456243422340",4));
        shop.endSaleTransaction(id);
    }

    @Test
    public void notEnoughQuantity() throws Exception {
        Integer id = shop.startSaleTransaction();
        shop.addProductToSale(id,"2424242424239",1);
        assertFalse(shop.deleteProductFromSale(id,"2424242424239",80));
        shop.endSaleTransaction(id);
    }

    @Test
    public void transactionNotOpen() throws Exception {
        Integer id = shop.startSaleTransaction();
        shop.addProductToSale(id,"2424242424239",1);
        shop.endSaleTransaction(id);

        assertFalse(shop.deleteProductFromSale(id,"2424242424239",1));
    }

    @Test
    public void correctCase() throws Exception {
        Integer id = shop.startSaleTransaction();
        shop.addProductToSale(id,"2424242424239",1);
        // TODO sistemare deleteProductFromSale
        assertTrue(shop.deleteProductFromSale(id,"2424242424239",1));
        shop.endSaleTransaction(id);
        //shop.logout();
        //shop.login("admin","ciao");
        //Integer productId = shop.getProductTypeByBarCode("2424242424239").getId();
        //shop.updateQuantity(productId,20);
    }
}
