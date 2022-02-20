package com.example.auth.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('VIEWONLY') or hasRole('CARDHOLDER') or hasRole('FULLACCESS')")
	public String userAccess() {
		return "You are logged in";
	}

	@GetMapping("/cardholder")
	@PreAuthorize("hasRole('CARDHOLDER')")
	public String cardholderAccess() {
		return "You have cardholder access.";
	}

	@GetMapping("/fullaccess")
	@PreAuthorize("hasRole('FULLACCESS')")
	public String fullAccess() {
		return "You have fullaccess.";
	}
}