package com.uphead.order_management.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.uphead.order_management.entity.User;
import com.uphead.order_management.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	 private  UserRepository userRepository;

	    public CustomUserDetailsService(UserRepository userRepository) {
	        this.userRepository = userRepository;
	    }
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 User user = userRepository
	                .findAll()
	                .stream()
	                .filter(existingUser ->
	                        existingUser.getEmail().equalsIgnoreCase(username))
	                .findFirst()
	                .orElseThrow(() ->
	                        new UsernameNotFoundException("User not found"));

	        return org.springframework.security.core.userdetails.User
	                .withUsername(user.getEmail())
	                .password(user.getPassword())
	                .roles(user.getRole().name())
	                .disabled(!user.getActive())
	                .build();
	}

}
