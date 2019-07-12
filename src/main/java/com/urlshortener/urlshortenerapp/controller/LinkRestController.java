package com.urlshortener.urlshortenerapp.controller;


import java.net.URI;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.urlshortener.urlshortenerapp.model.Link;
import com.urlshortener.urlshortenerapp.model.LinkRepository;
import com.urlshortener.urlshortenerapp.model.URL;
import com.urlshortener.urlshortenerapp.service.IDManager;

@RestController
public class LinkRestController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LinkRestController.class);
	
	private String[] schemes = {"http", "https"};
	private UrlValidator urlValidator = new UrlValidator(schemes);
	
	private LinkRepository linkRepository;
	private IDManager idManager;
	private RedisAtomicInteger counter;
		
	@Autowired
	private LinkRestController(LinkRepository linkRepository, IDManager idManager, RedisAtomicInteger counter) {
		this.linkRepository = linkRepository;
		this.idManager = idManager;
		this.counter = counter;
	}
		
	@GetMapping("/")
	public ModelAndView home() {
		return new ModelAndView("index.html");
	}
	
	@PostMapping("/v1/shorten")
	public ResponseEntity<String> add(@RequestBody URL url) {
		
		LOGGER.info(url.getUrl());
		
		if (urlValidator.isValid(url.getUrl())) {
			counter.incrementAndGet();
			
			LOGGER.info("Counter: " + counter.get());
			
			Link link = new Link(Integer.toString(counter.get()), url.getUrl());
			
			linkRepository.save(link);
			
			LOGGER.info("id: " + link.getId());
			LOGGER.info("url: " + link.getUrl());
			
			String uniqueId = idManager.generateUniqueID(Long.parseLong(link.getId()));
			
			// For tests
			if (uniqueId == null) {
				uniqueId = "abc";
			}
			
			LOGGER.info("uid: " + uniqueId);
			
			final String prefix = "http://localhost:8080/r/";
			HttpHeaders headers = new HttpHeaders();
			headers.set("uid", uniqueId);
			
			return new ResponseEntity<>(prefix + uniqueId, headers, HttpStatus.CREATED);

		}
		
		return new ResponseEntity<>("Invalid URL", HttpStatus.BAD_REQUEST);
	}
	
//	@PostMapping("/v1/shorten")
//	public ResponseEntity<String> add(@RequestBody URL url) {
//		
//		LOGGER.info(url.getUrl());
//		
//		if (urlValidator.isValid(url.getUrl())) {
//			
//			Link link = new Link(Integer.toString(count), url.getUrl());
//			count += 1;
//			linkRepository.save(link);
//			
//			LOGGER.info("id: " + link.getId());
//			LOGGER.info("url: " + link.getUrl());
//			
//			String uniqueId = idManager.generateUniqueID(Long.parseLong(link.getId()));
//			
//			LOGGER.info("uid: " + uniqueId);
//			
//			final String prefix = "http://localhost:8080/r/";
//			
//			return ResponseEntity.status(201).header("uid", uniqueId).body(prefix + uniqueId);
//
//		}
//		
//		return new ResponseEntity<>("Invalid URL", HttpStatus.BAD_REQUEST);
//	}
	
	@GetMapping("/r/{id}")
	public RedirectView link(@PathVariable("id") final String id) {
		
		LOGGER.info(id);
		
		long key = idManager.getKeyFromID(id);
		Link link = linkRepository.findById(Long.toString(key));
		
		RedirectView redirect = new RedirectView();	
		if (link != null) {
			redirect.setUrl(link.getUrl());
		} else {
			redirect.setUrl("/404");
		}

	    return redirect;
	}
	
	@GetMapping("/404")
	public ModelAndView pageNotFound() {
		return new ModelAndView("404.html");
	}
	
}