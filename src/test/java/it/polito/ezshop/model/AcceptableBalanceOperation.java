package it.polito.ezshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcceptableBalanceOperation {
    private it.polito.ezshop.model.BalanceOperation bo;
    private LocalDate now = LocalDate.now();

    @BeforeEach
    public void newSaleTransaction(){
        bo = new it.polito.ezshop.model.BalanceOperation(1,now,10.0,"DEBIT" );
    }

    @Test
    public void testGetBalanceId() {
        assertEquals(1, bo.getBalanceId());
    }
    @Test
    public void testGetDate() {
        assertEquals(now, bo.getDate());
    }
    @Test
    public void testGetMoney() {
        assertEquals(10.0, bo.getMoney());
    }
    @Test
    public void testGetType() {
        assertEquals("DEBIT", bo.getType());
    }

    @Test
    public void testSetBalanceId() {
        bo.setBalanceId(2);
        assertEquals(2, bo.getBalanceId());
    }
    @Test
    public void testSetDate() {
        now= LocalDate.now();
        bo.setDate(now);
        assertEquals(now, bo.getDate());
    }
    @Test
    public void testSetMoney() {
        bo.setMoney(11.0);
        assertEquals(11.0, bo.getMoney());
    }
    @Test
    public void testSetType() {
        bo.setType("DEBIT");
        assertEquals("DEBIT", bo.getType());
    }

}