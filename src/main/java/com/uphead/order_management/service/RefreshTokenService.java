package com.uphead.order_management.service;

import com.uphead.order_management.entity.RefreshToken;
import com.uphead.order_management.entity.User;

public interface RefreshTokenService {
	
	 RefreshToken createRefreshToken(User user);

	    RefreshToken verifyRefreshToken(String token);

	    void revokeToken(String token);

	    void revokeAllTokens(User user);

}
