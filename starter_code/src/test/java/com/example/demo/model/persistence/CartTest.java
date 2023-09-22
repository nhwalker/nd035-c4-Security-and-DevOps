package com.example.demo.model.persistence;

import static com.example.demo.TestUtils.*;

import com.example.demo.TestUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

public class CartTest {

    private TestUtils utils;

    @Test
    public void addItem(){
        Cart cart = new Cart();
        cart.addItem(item(2));
        assertEquals(2, cart.getTotal().intValueExact());
        cart.addItem(item(4));
        assertEquals(6, cart.getTotal().intValueExact());
    }

    @Test
    public void removeItem_present(){
        Item a=item(1);
        Item b=item(2);
        Item c=item(2);

        Cart cart = new Cart();
        cart.addItem(a);
        cart.addItem(b);
        cart.addItem(c);

        cart.removeItem(b);

        assertEquals(Arrays.asList(a,c),cart.getItems());
        assertEquals(3,cart.getTotal().intValueExact());
    }

    @Test
    public void removeItem_missing(){
        Item a=item(1);
        Item b=item(2);
        Item c=item(2);

        Cart cart = new Cart();
        cart.addItem(a);
        cart.addItem(c);

        cart.removeItem(b);

        assertEquals(Arrays.asList(a,c),cart.getItems());
        assertEquals(3,cart.getTotal().intValueExact());
    }

    @Test
    public void removeItem_empty(){
        Item a=item(1);

        Cart cart = new Cart();
        cart.removeItem(a);

        assertEquals(Collections.emptyList(),cart.getItems());
        assertEquals(0,cart.getTotal().intValueExact());
    }


}