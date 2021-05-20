package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AcceptableUpdateProduct {
    @Test
    public void testAuthorization() throws Exception{

        EZShop shop = new EZShop();
        assertThrows(UnauthorizedException.class,()->
                shop.createProductType("Latte", "11234567890125", 13.3, "Vecchio" )
        );
        shop.login("23","12345");
        assertThrows(UnauthorizedException.class,()->
                shop.createProductType("Latte", "11234567890125", 13.3, "Vecchio" )
        );
        //shop.close();
    }
    @Test
    public void testProductCode() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");

        assertThrows(InvalidProductCodeException.class,()->
                shop.createProductType("Latte", "", 13.3, "Vecchio" )
        );

        assertThrows(InvalidProductCodeException.class,()->
                shop.createProductType("Latte", null, 0.1, "Vecchio" )
        );
        assertThrows(InvalidProductCodeException.class,()->
                shop.createProductType("Latte", "12345678910", 0.1, "Vecchio" )
        );

    }
    @Test
    public void testPricePerUnit() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        assertThrows(InvalidPricePerUnitException.class,()->
                shop.createProductType("Latte", "11234567890125", -13.3, "Vecchio" )
        );

        assertThrows(InvalidPricePerUnitException.class,()->
                shop.createProductType("Biscotti", "11234567890125", 0.0, "Vecchio" )
        );
        //shop.close();
    }

    @Test
    public void testDescription() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");

        assertThrows(InvalidProductDescriptionException.class,()->
                shop.createProductType("", "11234567890125", 13.3, "Vecchio" )
        );

        assertThrows(InvalidProductDescriptionException.class,()->
                shop.createProductType(null, "11234567890125", 0.1, "Vecchio" )
        );
        ////shop.close();
    }

    @Test
    public void testProductId() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");

        assertThrows(InvalidProductIdException.class,()-> shop.updateProduct(null, "Latte", "11234567890125", 13.3, "Vecchio"));

        assertThrows(InvalidProductIdException.class,()-> shop.updateProduct(-5,"Latte", "11234567890125", 0.1, "Vecchio" ));

    }
    @Test
    public void testNoProductId() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");

        assertFalse(shop.updateProduct(433, "Latte", "11234567890125", 13.3, "Vecchio"));


    }

    @Test
    public void testCorrectCase() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        Integer id = shop.createProductType("Latte","4673387564579",21.0,"Ciao");
        assertTrue(shop.updateProduct(id, "Pomodori", "826427647372", 13.3, "Echo"));
        shop.deleteProductType(id);

    }

    @Test
    public void testSameBarcodePresent() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");
                assertFalse(shop.updateProduct(2, "Latte", "11234567890125", 13.3, "Echo"));


    }

    @Test
    public void testValidBarcode() {
        assertTrue(it.polito.ezshop.model.ProductType.validateProductCode("11234567890125"));
        assertFalse(it.polito.ezshop.model.ProductType.validateProductCode("12345678901234"));
        assertTrue(it.polito.ezshop.model.ProductType.validateProductCode("1234567890128"));
        assertTrue(it.polito.ezshop.model.ProductType.validateProductCode("123456789012"));
    }



}
