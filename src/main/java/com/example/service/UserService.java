package com.example.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.entity.User;



public interface UserService extends UserDetailsService {

	User getUser(long id);

	User save(User user);
	
	User getUserName(String name);
}
