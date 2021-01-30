package com.gameofthree.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/heartbeat")
public class ApplicationInfoController {

	@GetMapping("")
	public ResponseEntity<String> getById() {
		return ResponseEntity.ok("Application is up. Version 1.0");
	}

}
