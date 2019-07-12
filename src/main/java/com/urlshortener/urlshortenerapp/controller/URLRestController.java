package com.urlshortener.urlshortenerapp.controller;

import java.util.Map;
import com.urlshortener.urlshortenerapp.model.User;
import com.urlshortener.urlshortenerapp.model.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/user")
public class URLRestController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	public URLRestController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/add/{id}/{name}")
	public User add(@PathVariable("id") final String id, @PathVariable("name") final String name) {
		userRepository.save(new User(id, name, 20000L));
		return userRepository.findById(id);
	}

	@GetMapping("/update/{id}/{name}")
	public User update(@PathVariable("id") final String id, @PathVariable("name") final String name) {
		userRepository.update(new User(id, name, 1000L));
		return userRepository.findById(id);
	}

	@GetMapping("/all")
	public Map<String, User> all() {
		return userRepository.findAll();
	}

}
