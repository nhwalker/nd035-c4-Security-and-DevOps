package com.example.demo.model.persistence;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;


public class ItemTest {

    public void equals_acceptsNull(){
        Item a= new Item();
        a.setId(1L);
        assertFalse(a.equals(null));
    }
    @Test
    public void equality_noid() {
        Item a= new Item();
        Item b = new Item();
        assertEquals(a.hashCode(),b.hashCode());
        assertTrue(a.equals(b));
    }

    @Test
    public void equality_sameIdOnly() {
        Item a= new Item();
        Item b = new Item();
        a.setId(1L);
        b.setId(1L);

        a.setPrice(BigDecimal.valueOf(123));
        a.setDescription("foo");
        a.setName("bar");

        b.setPrice(BigDecimal.valueOf(1234));
        b.setDescription("foo2");
        b.setName("bar2");

        assertEquals(a.hashCode(),b.hashCode());
        assertTrue(a.equals(b));
    }
    @Test
    public void equality_differentId() {
        Item a= new Item();
        Item b = new Item();
        a.setId(1L);
        b.setId(2L);

        // Technically hashCode *could* be equal, but
        // in such a basic case should not be
        assertNotEquals(a.hashCode(),b.hashCode());

        assertFalse(a.equals(b));
    }
}