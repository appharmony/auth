package com.example.auth.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import com.example.auth.model.ERole;
import com.example.auth.model.Role;
import com.example.auth.model.User;
import com.example.auth.payload.request.LoginRequest;
import com.example.auth.payload.request.SignupRequest;
import com.example.auth.payload.response.JwtResponse;
import com.example.auth.payload.response.SignupResponse;
import com.example.auth.payload.response.MessageResponse;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.jwt.JwtUtils;
import com.example.auth.security.services.UserDetailsImpl;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		log.debug("Logging in user " + loginRequest.getUsername());
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt,
				userDetails.getId(),
				userDetails.getUsername(),
				userDetails.getEmail(),
				roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			log.debug("Email already in use!");
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(),
				signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		log.debug("Roles specified in the request: " + strRoles);
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_VIEWONLY)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			log.debug("No role specified. Adding default role.");
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
					case "fullaccess":
						Role fullaccess = roleRepository.findByName(ERole.ROLE_FULLACCESS)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(fullaccess);
						break;
					case "cardholder":
						Role cardholder = roleRepository.findByName(ERole.ROLE_CARDHOLDER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(cardholder);
						break;
					default:
						Role userRole = roleRepository.findByName(ERole.ROLE_VIEWONLY)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		User userSaved = userRepository.save(user);
		log.debug("User saved in database: " + user.getUsername() + user.getRoles());

		return ResponseEntity.ok().body(new SignupResponse(userSaved.getUsername(), userSaved.getEmail(), roles));
	}

	@PostMapping("/create-roles")
	public String createRoles() {
		long count = roleRepository.count();
		if (count == 0) {
			log.debug("No roles found in the database. Inserting them...");
			roleRepository.save(new Role(ERole.ROLE_VIEWONLY));
			roleRepository.save(new Role(ERole.ROLE_CARDHOLDER));
			roleRepository.save(new Role(ERole.ROLE_FULLACCESS));
		} else {
			log.debug("The roles are already present in the database");
		}
		return "Action complete";
	}
}