package com.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.representation.AuthorizationRequest;
import com.example.representation.UserResponse;
import com.example.service.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private UserService userService;

	public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	// @Secured("ROLE_ADMIN")
	@PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getUser(@PathVariable long id) {
		final User user = userService.getUser(id);

		if (user == null) {
			return ResponseEntity.notFound().build();
		}

		// SecurityContextHolder.getContext().getAuthentication().getName();

		UserResponse userResponse = UserMapper.toResponse(user);
		// return new
		// ResponseEntity<>(SecurityContextHolder.getContext().getAuthentication().getName(),
		// HttpStatus.OK);
		return new ResponseEntity<>(userResponse, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<User> saveUser(@RequestBody AuthorizationRequest userRequest) {

		userRequest.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
		final User userToSave = userService.save(UserMapper.toDomain(userRequest));

		return new ResponseEntity<>(userToSave, HttpStatus.OK);
	}
}
