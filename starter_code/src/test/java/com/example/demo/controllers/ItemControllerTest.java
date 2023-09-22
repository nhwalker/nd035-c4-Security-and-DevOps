package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper json;

    @Autowired
    private TestUtils utils;


    @Test
    public void getItems() throws Exception {
        HttpHeaders headers = utils.createAndLogin();

        String itemJson = mvc.perform(MockMvcRequestBuilders.get("/api/item")
                        .headers(headers))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Item> items= json.readValue(itemJson, new TypeReference<List<Item>>(){});
        assertEquals(2,items.size());
    }

    @Test
    public void getItemById() throws Exception {
        HttpHeaders headers = utils.createAndLogin();

        String itemJson = mvc.perform(MockMvcRequestBuilders.get("/api/item/1")
                        .headers(headers))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        Item item= json.readValue(itemJson, Item.class);

        assertEquals("Round Widget",item.getName());
        assertEquals(Long.valueOf(1),item.getId());
        assertNotNull(item.getPrice());
        assertNotNull(item.getDescription());
    }

    @Test
    public void getItemsByName() throws Exception {
        HttpHeaders headers = utils.createAndLogin();

        String itemJson = mvc.perform(MockMvcRequestBuilders.get("/api/item/name/Round Widget")
                        .headers(headers))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Item> items= json.readValue(itemJson, new TypeReference<List<Item>>(){});
        assertEquals(1,items.size());

        assertEquals("Round Widget",items.get(0).getName());
        assertEquals(Long.valueOf(1),items.get(0).getId());
        assertNotNull(items.get(0).getPrice());
        assertNotNull(items.get(0).getDescription());
    }

    @Test
    public void getItems_loginRequired() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/item"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void getItemById_loginRequired() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/item/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void getItemsByName_loginRequired() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/item/name/Round Widget"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}