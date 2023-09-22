package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest{
		@Autowired
		private MockMvc mvc;
		@Autowired
		private ObjectMapper json;

		@Autowired
		private TestUtils utils;

		@Test
		public void createuser_success() throws Exception {
			MvcResult result = utils.createUser(utils.newUserName(),"foobar123","foobar123");
			assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		}

		@Test
		public void createuser_missmatched_password() throws Exception {
			MvcResult result = utils.createUser(utils.newUserName(),"foobar123","foobar12345");
			assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
		}
		@Test
		public void createuser_short_password() throws Exception {
			MvcResult result = utils.createUser(utils.newUserName(),"abc","abc");
			assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
		}

		@Test
		public void getUserById() throws Exception {
			String name = utils.newUserName();
			User user = utils.createUser(name,"password");
			long id=user.getId();

			HttpHeaders authorization=utils.login(name,"password");

			String responseBody=mvc.perform(MockMvcRequestBuilders.get("/api/user/id/"+id).headers(authorization))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn().getResponse().getContentAsString();
			User responseUser=json.readValue(responseBody,User.class);

			assertEquals(user.getId(),responseUser.getId());
			assertEquals(name,responseUser.getUsername());
		}

		@Test
		public void getUserById_loginRequired() throws Exception {
			String name = utils.newUserName();
			User user = utils.createUser(name,"password");

			mvc.perform(MockMvcRequestBuilders.get("/api/user/id/"+user.getId()))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.status().isForbidden());
		}


		@Test
		public void getUserByName() throws Exception {
			String name = utils.newUserName();
			User user = utils.createUser(name,"password");
			long id=user.getId();

			HttpHeaders authorization=utils.login(name,"password");

			String responseBody=mvc.perform(MockMvcRequestBuilders.get("/api/user/"+name).headers(authorization))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn().getResponse().getContentAsString();
			User responseUser=json.readValue(responseBody,User.class);

			assertEquals(user.getId(),responseUser.getId());
			assertEquals(name,responseUser.getUsername());
		}

		@Test
		public void getUserByName_loginRequired() throws Exception {
			String name = utils.newUserName();
			User user = utils.createUser(name,"password");

			mvc.perform(MockMvcRequestBuilders.get("/api/user/"+name))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.status().isForbidden());
		}
}