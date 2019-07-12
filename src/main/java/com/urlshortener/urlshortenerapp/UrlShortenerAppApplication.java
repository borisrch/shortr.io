package com.urlshortener.urlshortenerapp;

import com.urlshortener.urlshortenerapp.model.Link;
import com.urlshortener.urlshortenerapp.model.User;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;

@SpringBootApplication
public class UrlShortenerAppApplication {
	
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		return new JedisConnectionFactory();
	}
	
	@Bean
	RedisTemplate<String, User> redisTemplate() {
		RedisTemplate<String, User> redisTemplate = new RedisTemplate<String, User>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}
	
	@Bean
	RedisTemplate<String, Link> redisLinkTemplate() {
		RedisTemplate<String, Link> redisLinkTemplate = new RedisTemplate<String, Link>();
		redisLinkTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisLinkTemplate;
	}
	
	@Bean
	public RedisAtomicInteger counter() {
		RedisAtomicInteger counter = new RedisAtomicInteger("counter", jedisConnectionFactory());
		counter.set(1);
		return counter;
	}

	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerAppApplication.class, args);
	}

}
