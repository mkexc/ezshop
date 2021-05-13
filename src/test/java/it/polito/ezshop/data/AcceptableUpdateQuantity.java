package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        assertFalse(shop.updateQuantity(1,-1 ));

        assertFalse(shop.updateQuantity(2,-30 ));

        assertFalse(shop.updateQuantity(132432,1 ));

    }
    @Test
    public void testCorrectCase() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");

        assertTrue(shop.updateQuantity(2,-1 ));


    }
}
