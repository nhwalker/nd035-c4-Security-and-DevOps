package com.example.demo;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;

@Service
public final class TestUtils {

    private final ObjectMapper json;
    private final MockMvc mvc;

    public TestUtils(ObjectMapper json, MockMvc mvc){
        this.json=json;
        this.mvc=mvc;
    }

    private static final AtomicLong userCount=new AtomicLong();

    private static final AtomicLong itemId=new AtomicLong();

    public static Item item(int value){
        Item item=new Item();
        item.setId(itemId.incrementAndGet());
        item.setPrice(BigDecimal.valueOf(value));
        return item;
    }

    public static String newUserName(){
        return "user"+userCount.getAndIncrement();
    }

    public HttpHeaders login(String name, String password) throws Exception{
        JSONObject request=new JSONObject();
        request.put("username",name);
        request.put("password",password);

        String authorization = mvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getHeaderValue("Authorization").toString();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",authorization);
        return headers;
    }

    public HttpHeaders createAndLogin() throws Exception{
        return createAndLogin(newUserName());
    }

    public HttpHeaders createAndLogin(String username) throws Exception{
        createUser(username,"password");
        return login(username,"password");
    }

    public User createUser(String name, String password) throws Exception {
        MvcResult result = createUser(name,password,password);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        String asJson=result.getResponse().getContentAsString();
        User created = json.readValue(asJson, User.class);
        return created;
    }

    public MvcResult createUser(String name, String password, String confirmPassword) throws Exception {
        CreateUserRequest createUserRequest=new CreateUserRequest();
        createUserRequest.setUsername(name);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(confirmPassword);
        String requestJson = json.writeValueAsString(createUserRequest);

        MockHttpServletRequestBuilder request=MockMvcRequestBuilders.post("/api/user/create").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson);

        return mvc.perform(request).andReturn();
    }

}
