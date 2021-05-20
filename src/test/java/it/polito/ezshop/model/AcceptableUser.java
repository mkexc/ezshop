package it.polito.ezshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AcceptableUser {

    private it.polito.ezshop.model.User user;

    @BeforeEach
    public void newUser() {
        user = new it.polito.ezshop.model.User(1, "ciao", "2020", "Cashier");
    }

    @Test
    public void testGetId() {
        assertEquals(1, user.getId());
    }

    @Test
    public void testGetUsername() {
        assertEquals("ciao", user.getUsername());
    }

    @Test
    public void testGetPassword(){
        assertEquals("2020", user.getPassword());
    }

    @Test
    public void testGetRole(){
        assertEquals("Cashier", user.getRole());
    }

    @Test
    public void testSetId() {
        user.setId(2);
        assertEquals(2, user.getId());
    }

    @Test
    public void testSetUsername() {
        user.setUsername("newUsername");
        assertEquals("newUsername", user.getUsername());
    }

    @Test
    public void testSetPassword() {
        user.setPassword("newPass");
        assertEquals("newPass", user.getPassword());
    }
    @Test
    public void testSetRole() {
        user.setRole("Administrator");
        assertEquals("Administrator", user.getRole());
    }

}
