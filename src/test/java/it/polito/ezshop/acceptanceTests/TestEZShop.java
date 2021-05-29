package it.polito.ezshop.acceptanceTests;


import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestEZShop {
    private static EZShopInterface shop;
    private int adminId;

    @BeforeClass
    public static void setUpEzShop() {
        shop = new EZShop();
    }

    @AfterClass
    public static void clearEzShop(){
        shop.reset();
    }

    @Before
    public void setup() {
        shop.reset();
        try {
            adminId = shop.createUser("admin","ciao","Administrator");
            shop.login("admin","ciao");
        } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void authTest() throws Exception
    {
        shop.logout();
        assertThrows(UnauthorizedException.class, () -> shop.addProductToSale(1,"3456243422340",3));
        shop.login("admin","ciao");
    }

    @Test
    public void invalidTransactionId()
    {
        assertThrows(InvalidTransactionIdException.class, () -> shop.addProductToSale(0,"3456243422340",3));
        assertThrows(InvalidTransactionIdException.class, () -> shop.addProductToSale(-1,"3456243422340",3));
        assertThrows(InvalidTransactionIdException.class, () -> shop.addProductToSale(null,"3456243422340",3));
    }

    @Test
    public void invalidProductCodeException()
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
        shop.logout();
        shop.login("admin","ciao");
        Integer idProduct = shop.createProductType("Vino","2424242424239",10.0,"Buono");
        shop.updatePosition(idProduct,"12-ac-12");
        shop.updateQuantity(idProduct,70);
        shop.logout();
        shop.login("23", "12345");
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
        shop.logout();
        shop.login("admin","ciao");
        Integer id = shop.startSaleTransaction();
        Integer idProduct = shop.createProductType("Vino","2424242424239",10.0,"Buono");
        shop.updatePosition(idProduct,"14-Boh-15");
        shop.updateQuantity(idProduct,70);
        shop.logout();
        shop.login("23","12345");
        assertTrue(shop.addProductToSale(id,"2424242424239",20));
        shop.endSaleTransaction(id);
        shop.logout();
        shop.login("admin","ciao");
        Integer productId = shop.getProductTypeByBarCode("2424242424239").getId();
        shop.updateQuantity(productId,20);
    }
}
