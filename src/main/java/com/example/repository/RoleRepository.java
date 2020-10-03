package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>  {
	
	Role getRoleById(long id);

	Role findByName(String name);

}
