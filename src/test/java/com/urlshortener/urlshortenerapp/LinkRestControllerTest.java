package com.urlshortener.urlshortenerapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urlshortener.urlshortenerapp.controller.LinkRestController;
import com.urlshortener.urlshortenerapp.model.LinkRepository;
import com.urlshortener.urlshortenerapp.model.LinkRepositoryImpl;
import com.urlshortener.urlshortenerapp.model.URL;
import com.urlshortener.urlshortenerapp.service.IDManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(LinkRestController.class)
public class LinkRestControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private LinkRepository repository;
	
	@MockBean
	private IDManager idManager;
	
	@Autowired
	private RedisAtomicInteger counter;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LinkRestController.class);
	
	@Test
	public void LinkShouldBeCreatedWhenValidUrlInRequest() throws Exception {
		
		URL url = new URL();
		url.setUrl("http://www.google.com");
	
		mvc.perform(post("/v1/shorten")
				.content(asJsonString(url))
                .contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

	}
	
	@Test
	public void LinkShouldBeRedirectedWhenInvalidIdInRequest() throws Exception {
		mvc.perform(get("/r/12cae8ed2f"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/404"));
	}
		
	public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
