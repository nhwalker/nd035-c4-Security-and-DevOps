package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.requests.ModifyCartRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper json;

    @Autowired
    private TestUtils utils;

    @Test
    public void addTocart() throws Exception {
        String username=TestUtils.newUserName();
        HttpHeaders auth= utils.createAndLogin(username);

        ModifyCartRequest request=new ModifyCartRequest();
        request.setUsername(username);
        request.setItemId(1);
        request.setQuantity(2);

        String cartJson = mvc.perform(MockMvcRequestBuilders.post("/api/cart/addToCart")
                        .headers(auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        Cart cart=json.readValue(cartJson,Cart.class);
        long inCart = cart.getItems().stream().filter(x->x.getId().longValue()==1).count();
        assertEquals(2,inCart);
    }

    @Test
    public void removeFromcart() throws Exception {
        String username=TestUtils.newUserName();
        HttpHeaders auth= utils.createAndLogin(username);

        ModifyCartRequest addRequest=new ModifyCartRequest();
        addRequest.setUsername(username);
        addRequest.setItemId(1);
        addRequest.setQuantity(2);

        ModifyCartRequest removeRequest=new ModifyCartRequest();
        removeRequest.setUsername(username);
        removeRequest.setItemId(1);
        removeRequest.setQuantity(1);

        // Put items in cart
        mvc.perform(MockMvcRequestBuilders.post("/api/cart/addToCart")
                        .headers(auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(addRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Take items out
        String cartJson =mvc.perform(MockMvcRequestBuilders.post("/api/cart/removeFromCart")
                        .headers(auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(removeRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        Cart cart=json.readValue(cartJson,Cart.class);
        long inCart = cart.getItems().stream().filter(x->x.getId().longValue()==1).count();
        assertEquals(1,inCart);
    }

    @Test
    public void addTocart_loginRequired() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/cart/addToCart"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void removeFromcart_loginRequired() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/cart/removeFromCart"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}