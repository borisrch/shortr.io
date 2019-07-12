package com.urlshortener.urlshortenerapp.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController {

	private static final String PATH = "/error";
	
	@RequestMapping(value = PATH)
	public ModelAndView error() {
		return new ModelAndView("404.html");
	}
	
	@Override
	public String getErrorPath() {
		return PATH;
	}

}
