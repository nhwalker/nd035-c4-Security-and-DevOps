package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.ModifyCartRequest;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper json;

    @Autowired
    private TestUtils utils;

    @Test
    public void submit() throws Exception {
        String username=TestUtils.newUserName();
        HttpHeaders auth=utils.createAndLogin(username);

        // Add item to cart
        ModifyCartRequest request=new ModifyCartRequest();
        request.setUsername(username);
        request.setItemId(1);
        request.setQuantity(1);
        mvc.perform(MockMvcRequestBuilders.post("/api/cart/addToCart")
                        .headers(auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Submit Order
        String orderJson=mvc.perform(MockMvcRequestBuilders.post("/api/order/submit/"+username).headers(auth))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Got back order
        UserOrder order = json.readValue(orderJson,UserOrder.class);
        assertNotNull(order.getId());
        assertFalse(order.getItems().isEmpty());
    }

    @Test
    public void getOrdersForUser() throws Exception {
        String username=TestUtils.newUserName();
        HttpHeaders auth=utils.createAndLogin(username);

        // Add item to cart
        ModifyCartRequest request=new ModifyCartRequest();
        request.setUsername(username);
        request.setItemId(1);
        request.setQuantity(1);
        mvc.perform(MockMvcRequestBuilders.post("/api/cart/addToCart")
                        .headers(auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Submit Order 1
        mvc.perform(MockMvcRequestBuilders.post("/api/order/submit/"+username).headers(auth))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Add item to cart
        request.setItemId(2);
        request.setQuantity(1);
        mvc.perform(MockMvcRequestBuilders.post("/api/cart/addToCart")
                        .headers(auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Submit Order 2
        mvc.perform(MockMvcRequestBuilders.post("/api/order/submit/"+username).headers(auth))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Get order history
        String orderHistoryJson=mvc.perform(MockMvcRequestBuilders.get("/api/order/history/"+username).headers(auth))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<UserOrder> orders=json.readValue(orderHistoryJson, new TypeReference<List<UserOrder>>() {
        });
        assertEquals(2,orders.size());
    }

    @Test
    public void submit_loginRequired() throws Exception {
        String username=TestUtils.newUserName();
        utils.createUser(username,"password");
        mvc.perform(MockMvcRequestBuilders.post("/api/order/submit/"+username))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void getOrdersForUser_loginRequired() throws Exception {
        String username=TestUtils.newUserName();
        utils.createUser(username,"password");
        mvc.perform(MockMvcRequestBuilders.get("/api/order/history/"+username))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}