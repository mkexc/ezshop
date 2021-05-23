package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptableAddProductToSale {
    EZShop shop;

    @Before
    public void beforeEach() throws Exception
    {
        shop = new EZShop();
        shop.login("23","12345");
    }

    @After
    public void afterEach()
    {
        shop.logout();
    }

    @Test
    public void authTest() throws Exception
    {
        shop.logout();
        assertThrows(UnauthorizedException.class, () -> shop.addProductToSale(1,"278732878273",3));
        shop.login("23","12345");
    }

    @Test
    public void invalidTransactionId() throws Exception
    {
        assertThrows(InvalidTransactionIdException.class, () -> shop.addProductToSale(0,"3456243422340",3));
        assertThrows(InvalidTransactionIdException.class, () -> shop.addProductToSale(-1,"3456243422340",3));
        assertThrows(InvalidTransactionIdException.class, () -> shop.addProductToSale(null,"3456243422340",3));
    }

    @Test
    public void invalidProductCodeException() throws Exception
    {
        assertThrows(InvalidProductCodeException.class, () -> shop.addProductToSale(1,"278732878273",3));
        assertThrows(InvalidProductCodeException.class, () -> shop.addProductToSale(1,"2121",3));
        assertThrows(InvalidProductCodeException.class, () -> shop.addProductToSale(1,"27873287827362737",3));
        assertThrows(InvalidProductCodeException.class, () -> shop.addProductToSale(1,"",3));
        assertThrows(InvalidProductCodeException.class, () -> shop.addProductToSale(1,null,3));
    }

    @Test
    public void invalidQuantity() throws Exception
    {
        Integer id = shop.startSaleTransaction();
        assertThrows(InvalidQuantityException.class, () -> shop.addProductToSale(id,"2424242424239",-1));
        shop.endSaleTransaction(id);
    }

    @Test
    public void nonExistingProductCode() throws Exception
    {
        Integer id = shop.startSaleTransaction();
        assertFalse(shop.addProductToSale(id,"3456243422340",4));
        shop.endSaleTransaction(id);
    }

    @Test
    public void notEnoughQuantity() throws Exception
    {
        Integer id = shop.startSaleTransaction();
        assertFalse(shop.addProductToSale(id,"2424242424239",80));
        shop.endSaleTransaction(id);
    }

    @Test
    public void notStartedSaleTransaction() throws Exception
    {
        assertFalse(shop.addProductToSale(99999,"2424242424239",70));
    }

    @Test
    public void correctCase() throws Exception
    {
        Integer id = shop.startSaleTransaction();
        assertFalse(shop.addProductToSale(id,"2424242424239",20));
        shop.endSaleTransaction(id);
        shop.logout();
        shop.login("admin","ciao");
        Integer productId = shop.getProductTypeByBarCode("2424242424239").getId();
        shop.updateQuantity(productId,20);
    }
}
