package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class AcceptableUpdateQuantity {
    @Test
    public void testAuthorization() throws Exception{

        EZShop shop = new EZShop();
        assertThrows(UnauthorizedException.class,()->
                shop.updateQuantity(1,1)
        );
        shop.login("23","12345");
        assertThrows(UnauthorizedException.class,()->
                shop.updateQuantity(1,1)
        );

    }
    @Test
    public void testProductId() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");

        assertThrows(InvalidProductIdException.class,()-> shop.updateQuantity(null,1));

        assertThrows(InvalidProductIdException.class,()-> shop.updateQuantity(-5,1 ));

    }
    @Test
    public void testToBeAdded() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        Integer id= shop.createProductType("Temp","236547234749",21,"Boh");
        shop.updatePosition(id,"19-per-20");
        assertFalse(shop.updateQuantity(id,-1 ));
        shop.deleteProductType(id);

        assertFalse(shop.updateQuantity(132432,1 ));
    }
    @Test
    public void testCorrectCase() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        Integer id= shop.createProductType("Temp","357424672543",21,"Boh");
        shop.updatePosition(id,"19-per-19");
        assertTrue(shop.updateQuantity(id,100));
        shop.deleteProductType(id);

    }
}
