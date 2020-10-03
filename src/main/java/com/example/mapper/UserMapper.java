package com.example.mapper;

import com.example.entity.User;
import com.example.representation.AuthorizationRequest;
import com.example.representation.UserResponse;

public class UserMapper {
	
	private UserMapper() {
	}

	public static UserResponse toResponse(User user) {
		UserResponse ur = new UserResponse();
		ur.setId(user.getId());
		ur.setName(user.getName());
		// return UserResponse.builder().name(user.getName()).id(user.getId()).build();
		return ur;
	}

	public static User toDomain(AuthorizationRequest authorizationRequest) {
		User u = new User();
		u.setName(authorizationRequest.getUserName());
		u.setPassword(authorizationRequest.getPassword());
		// return
		// User.builder().name(authorizationRequest.getUserName()).password(authorizationRequest.getPassword()).build();
		return u;
	}
}
