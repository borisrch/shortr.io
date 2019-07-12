package com.urlshortener.urlshortenerapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingRestController {

	private static final String greeting = "Hello World";

	@GetMapping("/hello")
	public ResponseEntity<String> hello() {
		return new ResponseEntity<>(greeting, HttpStatus.OK);
	}
}
