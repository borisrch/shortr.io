package com.urlshortener.urlshortenerapp.service;

public interface IDManager {
	public String generateUniqueID(Long id);
	public Long getKeyFromID(String id);
}
