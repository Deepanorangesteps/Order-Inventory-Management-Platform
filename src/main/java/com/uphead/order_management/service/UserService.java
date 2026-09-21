package com.uphead.order_management.service;

import java.util.List;

import com.uphead.order_management.entity.User;
import com.uphead.order_management.request.UserRequest;
import com.uphead.order_management.response.UserResponse;

public interface UserService {

	User createUser(UserRequest request);

	UserResponse getUserById(Long id);
	
	public List<UserResponse> getAllUsers() ;

}
