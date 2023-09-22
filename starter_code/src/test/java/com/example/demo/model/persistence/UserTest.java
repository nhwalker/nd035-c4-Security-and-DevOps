package com.example.demo.model.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void no_password_in_json() throws JsonProcessingException {
        User user = new User();
        user.setUsername("bill");
        user.setPassword("abcdefg");
        user.setId(3);

        ObjectMapper mapper = new ObjectMapper();
        String json=mapper.writeValueAsString(user);

        assertFalse(json.contains("abcdefg"));
    }

    @Test
    public void no_cart_in_json() throws JsonProcessingException {
        User user = new User();
        user.setUsername("bill");
        user.setId(3);
        user.setCart(new Cart());
        user.getCart().setId(12345L);

        ObjectMapper mapper = new ObjectMapper();
        String json=mapper.writeValueAsString(user);

        assertFalse(json.contains("12345"));
    }
}