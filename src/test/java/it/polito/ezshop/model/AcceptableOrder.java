package it.polito.ezshop.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class AcceptableOrder {

    it.polito.ezshop.model.Order order;

    @Before
    public void newOrder ()
    {
        order = new Order(1, "2143325343648", 25.8, 23, "ISSUED");
    }

    @Test
    public void getId()
    {
        assertEquals(1,order.getOrderId().intValue());
    }

    @Test
    public void getProductCode()
    {
        assertEquals("2143325343648",order.getProductCode());
    }

    @Test
    public void getPrice()
    {
        assertEquals(25.8,order.getPricePerUnit(),0);
    }

    @Test
    public void getQuantity()
    {
        assertEquals(23,order.getQuantity());
    }

    @Test
    public void getStatus()
    {
        assertEquals("ISSUED",order.getStatus());
    }

    @Test
    public void getBalanceId()
    {
        assertEquals(0, order.getBalanceId().intValue());
    }

    @Test
    public void setBalanceId()
    {
        order.setBalanceId(2);
        assertEquals(2,order.getBalanceId().intValue());
    }

    @Test
    public void setId()
    {
        order.setOrderId(2);
        assertEquals(2,order.getOrderId().intValue());
    }

    @Test
    public void setProductCode()
    {
        order.setProductCode("11234567890125");
        assertEquals("11234567890125",order.getProductCode());
    }

    @Test
    public void setPrice()
    {
        order.setPricePerUnit(28.9);
        assertEquals(28.9,order.getPricePerUnit(),0);
    }


    @Test
    public void setQuantity()
    {
        order.setQuantity(22);
        assertEquals(22,order.getQuantity());
    }

    @Test
    public void setStatus()
    {
        order.setStatus("PAYED");
        assertEquals("PAYED",order.getStatus());
    }

    @Test
    public void newEmptyOrder()
    {
        it.polito.ezshop.model.Order or= new Order(5);
        assertEquals(5,or.getOrderId().intValue());
    }

    @Test
    public void newFullOrder ()
    {
        it.polito.ezshop.model.Order or = new Order(2, "2143325343648", 25.8, 23, "ISSUED");
        assertEquals(2,or.getOrderId().intValue());
    }

}
