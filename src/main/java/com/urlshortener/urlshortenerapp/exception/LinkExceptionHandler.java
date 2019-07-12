package com.urlshortener.urlshortenerapp.exception;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import redis.clients.jedis.exceptions.JedisDataException;

@ControllerAdvice
@RestController
public class LinkExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(LinkNotFoundException.class)
	public ResponseEntity<Object> linkNotFound(LinkNotFoundException exception) {
		return new ResponseEntity<>("Link not found.", HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(RedisConnectionFailureException.class)
	public ResponseEntity<String> redisFailedToConnect(RedisConnectionFailureException e) {
		return new ResponseEntity<>("Failed to connect to database service.", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(JedisDataException.class)
	public ResponseEntity<String> jedisDataException(JedisDataException e) {
		return new ResponseEntity<>("Internal server error. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
