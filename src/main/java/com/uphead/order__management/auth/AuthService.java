package com.uphead.order__management.auth;

import com.uphead.order_management.request.LoginRequest;
import com.uphead.order_management.response.LoginResponse;

public interface AuthService {
	LoginResponse login(LoginRequest request);
	
	LoginResponse refreshAccessToken(String refreshToken);

    void logout(String refreshToken);

}
