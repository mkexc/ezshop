package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AcceptableUpdatePosition {
    @Test
    public void testAuthorization() throws Exception{

        EZShop shop = new EZShop();
        assertThrows(UnauthorizedException.class,()->
                shop.updatePosition(1,"332-casa-321")
        );
        shop.login("23","12345");
        assertThrows(UnauthorizedException.class,()->
                shop.updatePosition(1,"332-casa-321")
        );

    }
    @Test
    public void testProductId() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");

        assertThrows(InvalidProductIdException.class,()-> shop.updatePosition(null,"332-casa-321"));

        assertThrows(InvalidProductIdException.class,()-> shop.updatePosition(-1,"332-casa-321"));

    }

    @Test
    public void testCorrectPosition() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");

        assertThrows(InvalidLocationException.class,()-> shop.updatePosition(1,"332-331-321"));
        assertThrows(InvalidLocationException.class,()-> shop.updatePosition(1,"casa-331-casa"));
        assertThrows(InvalidLocationException.class,()-> shop.updatePosition(1,"33a2-3a31-3a21"));


    }
    @Test
    public void testDuplicatePosition() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");
        shop.updatePosition(1,"12-as-12" );
        assertFalse(shop.updatePosition(3,"12-as-12" ));
        shop.updatePosition(1,"14-per-14");
    }

    @Test
    public void testCorrectCase() throws Exception{

        EZShop shop = new EZShop();
        shop.login("admin","ciao");

        assertTrue(shop.updatePosition(1,"13-a-13" ));

    }
}
