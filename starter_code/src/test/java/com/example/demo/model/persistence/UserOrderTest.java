package com.example.demo.model.persistence;
import static com.example.demo.TestUtils.*;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

public class UserOrderTest {

    @Test
    public void createFromCart() {
        User user = new User();
        user.setId(1);
        user.setUsername("bob");
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setId(2L);
        cart.addItem(item(1));
        cart.addItem(item(2));
        cart.addItem(item(3));

        UserOrder order = UserOrder.createFromCart(cart);
        assertEquals(user,order.getUser());
        assertEquals(cart.getItems(),order.getItems());
        assertEquals(6,order.getTotal().intValueExact());
    }

}