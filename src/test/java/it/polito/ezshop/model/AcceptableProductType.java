package it.polito.ezshop.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class AcceptableProductType {

     it.polito.ezshop.model.ProductType productType;

    @BeforeEach
    public void newProductType()
    {
        productType = new it.polito.ezshop.model.ProductType(1, "2143325343648", "CiaoDescrizione", 23.5,21, "NoteOn","Shelf");
    }

    @Test
    public void testGetQuantity() {
        assertEquals(1,productType.getId().intValue());
    }

    @Test
    public void testSetQuantity() {
        productType.setQuantity(22);
        assertEquals(22,productType.getQuantity());
    }

    @Test
    public void testGetLocation() {
        assertEquals("Shelf",productType.getLocation());
    }

    @Test
    public void testSetLocation() {
        productType.setLocation("Boh");
        assertEquals("Boh",productType.getLocation());
    }

    @Test
    public void testGetNote() {
        assertEquals("NoteOn",productType.getNote());
    }

    @Test
    public void testSetNote() {
        productType.setNote("Note2");
        assertEquals("Note2",productType.getNote());
    }

    @Test
    public void testGetProductDescription() {
        assertEquals("CiaoDescrizione",productType.getProductDescription());
    }

    @Test
    public void testSetProductDescription() {
        productType.setProductDescription("CiaoDescrizione2");
        assertEquals("CiaoDescrizione2",productType.getProductDescription());
    }

    @Test
    public void testGetBarCode() {
        assertEquals("2143325343648",productType.getBarCode());
    }

    @Test
    public void testSetBarCode() {
        productType.setBarCode("11234567890125");
        assertEquals("11234567890125",productType.getBarCode());
    }

    @Test
    public void testGetPricePerUnit() {
        assertEquals(23.5,productType.getPricePerUnit());
    }

    @Test
    public void testSetPricePerUnit() {
        productType.setPricePerUnit(24.9);
        assertEquals(24.9,productType.getPricePerUnit());
    }

    @Test
    public void testGetId() {
        assertEquals(1,productType.getId());
    }

    @Test
    public void setId() {
        productType.setId(2);
        assertEquals(2,productType.getId());
    }

    @Test
    public void testValidationProductCode()
    {
        assertTrue(it.polito.ezshop.model.ProductType.validateProductCode("11234567890125"));
        assertFalse(it.polito.ezshop.model.ProductType.validateProductCode("12345678901234"));
        assertTrue(it.polito.ezshop.model.ProductType.validateProductCode("1234567890128"));
        assertTrue(it.polito.ezshop.model.ProductType.validateProductCode("123456789012"));
        assertFalse(it.polito.ezshop.model.ProductType.validateProductCode("123456789"));
        assertFalse(it.polito.ezshop.model.ProductType.validateProductCode("12345678901111112"));
        assertTrue(it.polito.ezshop.model.ProductType.validateProductCode("11234567890200"));


    }


}
