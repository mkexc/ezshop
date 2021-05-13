package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AcceptableGetProductTypeByBarcode {
    @Test
    public void testAuthorization() throws Exception{

        EZShop shop = new EZShop();
        assertThrows(UnauthorizedException.class,()->
                shop.getProductTypeByBarCode("00012345678905")
        );
        shop.login("23","12345");
        assertThrows(UnauthorizedException.class,()->
                shop.getProductTypeByBarCode("00012345678905")
        );

    }

    @Test
    public void testProductCode() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");

        assertThrows(InvalidProductCodeException.class,()-> shop.getProductTypeByBarCode(""));

        assertThrows(InvalidProductCodeException.class,()-> shop.getProductTypeByBarCode(null));

        assertThrows(InvalidProductCodeException.class,()-> shop.getProductTypeByBarCode("123345657686542"));

        assertThrows(InvalidProductCodeException.class,()-> shop.getProductTypeByBarCode("1dasdadsasdas"));

        // SAME BARCODE PRESENT
        assertNull(shop.getProductTypeByBarCode("aaa1232143214421"));
    }

    @Test
    public void testNoProduct() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        assertNull(shop.getProductTypeByBarCode("1234565432436"));

    }

    @Test
    public void testCorrectCase() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        assertInstanceOf(it.polito.ezshop.model.ProductType.class,shop.getProductTypeByBarCode("11234567890125"));

    }

}
