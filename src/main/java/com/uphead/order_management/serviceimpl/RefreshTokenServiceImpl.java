package com.uphead.order_management.serviceimpl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.uphead.order_management.entity.RefreshToken;
import com.uphead.order_management.entity.User;
import com.uphead.order_management.repository.RefreshTokenRepository;
import com.uphead.order_management.service.RefreshTokenService;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {private final RefreshTokenRepository refreshTokenRepository;
private final long refreshTokenExpiration;

public RefreshTokenServiceImpl(
		RefreshTokenRepository refreshTokenRepository,
		@Value("${jwt.refresh-token-expiration}")
		long refreshTokenExpiration) {

	this.refreshTokenRepository = refreshTokenRepository;
	this.refreshTokenExpiration = refreshTokenExpiration;
}

@Override
public RefreshToken createRefreshToken(User user) {

	RefreshToken refreshToken = new RefreshToken();

	refreshToken.setToken(UUID.randomUUID().toString());
	refreshToken.setUser(user);

	refreshToken.setExpiresAt(
			LocalDateTime.now()
			.plusSeconds(refreshTokenExpiration / 1000)
			);

	refreshToken.setRevoked(false);

	return refreshTokenRepository.save(refreshToken);
}

@Override
public RefreshToken verifyRefreshToken(String token) {

	RefreshToken refreshToken =
			refreshTokenRepository
			.findByTokenAndRevokedFalse(token)
			.orElseThrow(() ->
			new RuntimeException(
					"Invalid refresh token"
					));

	if (refreshToken.isExpired()) {

		refreshToken.setRevoked(true);

		refreshTokenRepository.save(refreshToken);

		throw new RuntimeException(
				"Refresh token has expired"
				);
	}

	return refreshToken;
}

@Override
public void revokeToken(String token) {

	refreshTokenRepository
	.findByToken(token)
	.ifPresent(refreshToken -> {

		refreshToken.setRevoked(true);

		refreshTokenRepository.save(refreshToken);
	});
}

@Override
public void revokeAllTokens(User user) {

	refreshTokenRepository.deleteByUser(user);
}

}
