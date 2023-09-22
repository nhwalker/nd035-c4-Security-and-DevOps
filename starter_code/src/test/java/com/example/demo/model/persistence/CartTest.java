package com.example.demo.model.persistence;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

public class CartTest {
    private static final AtomicLong itemId=new AtomicLong();
    @Test
    public void test_addItem(){
        Cart cart = new Cart();
        cart.addItem(item(2));
        assertEquals(2, cart.getTotal().intValueExact());
        cart.addItem(item(4));
        assertEquals(6, cart.getTotal().intValueExact());
    }

    @Test
    public void test_removeItem_present(){
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
    public void test_removeItem_missing(){
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
    public void test_removeItem_empty(){
        Item a=item(1);

        Cart cart = new Cart();
        cart.removeItem(a);

        assertEquals(Collections.emptyList(),cart.getItems());
        assertEquals(0,cart.getTotal().intValueExact());
    }

    private static Item item(int value){
        Item item=new Item();
        item.setId(itemId.incrementAndGet());
        item.setPrice(BigDecimal.valueOf(value));
        return item;
    }
}