package com.example.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.mapper.UserDetailsMapper;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;

@Service("userDetailsService")
public class UserServiceImpl implements UserService {

	private RoleRepository roleRepository;

	private UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		final User retrievedUser = userRepository.findByName(userName);
		if (retrievedUser == null) {
			throw new UsernameNotFoundException("Invalid username or password");
		}

		return UserDetailsMapper.build(retrievedUser);
	}

	@Override
	public User getUser(long id) {
		return userRepository.findById(id);
	}
	
	@Override
	public User getUserName(String name) {
		return userRepository.findByName(name);
	}

	@Override
	public User save(User user) {

		Role userRole = roleRepository.findByName("USER");
		Set<Role> roles = new HashSet<>();
		roles.add(userRole);

		User userToSave = new User();
		userToSave.setId(user.getId());
		userToSave.setName(user.getName());
		userToSave.setPassword(user.getPassword());
		userToSave.setEnabled(user.isEnabled());
		userToSave.setRoles(roles);

		return userRepository.save(userToSave);

	}

}
