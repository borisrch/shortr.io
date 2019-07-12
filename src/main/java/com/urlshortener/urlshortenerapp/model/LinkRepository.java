package com.urlshortener.urlshortenerapp.model;

public interface LinkRepository {
	void save(Link link);
	Link findById(String id);
}
