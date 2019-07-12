package com.urlshortener.urlshortenerapp.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.urlshortener.urlshortenerapp.exception.LinkNotFoundException;

@Repository
public class LinkRepositoryImpl implements LinkRepository {

	private RedisTemplate<String, Link> redisLinkTemplate;
	private HashOperations hashOperations;
	
	@Autowired
	public LinkRepositoryImpl(RedisTemplate<String, Link> redisLinkTemplate) {
		this.redisLinkTemplate = redisLinkTemplate;
		this.hashOperations = redisLinkTemplate.opsForHash();
	}
	
	public void save(Link link) {
		hashOperations.put("LINK", link.getId(), link);
	}
	
	public Link findById(String id) {
		Object result = hashOperations.get("LINK", id);
		return (Link)hashOperations.get("LINK", id);
	}
	
}
