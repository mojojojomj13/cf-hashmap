package com.techno.broker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This is the home controller
 * 
 * @author Prithvish Mukherjee
 *
 */
@Controller
public class HomeController {

	@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> home() {
		return new ResponseEntity<String>("cf-hashmap-service", HttpStatus.OK);
	}
}
