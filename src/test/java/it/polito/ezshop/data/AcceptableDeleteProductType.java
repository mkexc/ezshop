package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AcceptableDeleteProductType {
    @Test
    public void testAuthorization() throws Exception{

        EZShop shop = new EZShop();
        assertThrows(UnauthorizedException.class,()->
                shop.deleteProductType(1)
        );
        shop.login("23","12345");
        assertThrows(UnauthorizedException.class,()->
                shop.deleteProductType(1)
        );


    }
    @Test
    public void testProductId() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");

        assertThrows(InvalidProductIdException.class,()-> {
            shop.deleteProductType(-1);
        });

        assertThrows(InvalidProductIdException.class,()->{
            shop.deleteProductType(null );
        });

    }

    @Test
    public void testNoIdToDelete() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");

        assertFalse(shop.deleteProductType(432));


    }
    @Test
    public void testCorrectCase() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");

        assertTrue(shop.deleteProductType(1));


    }
}
