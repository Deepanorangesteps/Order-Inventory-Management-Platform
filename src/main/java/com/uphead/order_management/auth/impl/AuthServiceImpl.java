package com.uphead.order_management.auth.impl;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uphead.order__management.auth.AuthService;
import com.uphead.order_management.entity.Organization;
import com.uphead.order_management.entity.RefreshToken;
import com.uphead.order_management.entity.User;
import com.uphead.order_management.repository.OrganizationRepository;
import com.uphead.order_management.repository.UserRepository;
import com.uphead.order_management.request.LoginRequest;
import com.uphead.order_management.response.LoginResponse;
import com.uphead.order_management.security.JwtService;
import com.uphead.order_management.service.RefreshTokenService;

@Service
public class AuthServiceImpl implements AuthService{

	  private final UserRepository userRepository;
	    private final OrganizationRepository organizationRepository;
	    private final PasswordEncoder passwordEncoder;
	    private final JwtService jwtService;
	    private final RefreshTokenService refreshTokenService;

	    public AuthServiceImpl(
	            UserRepository userRepository,
	            OrganizationRepository organizationRepository,
	            PasswordEncoder passwordEncoder,
	            JwtService jwtService,
	            RefreshTokenService refreshTokenService) {

	        this.userRepository = userRepository;
	        this.organizationRepository = organizationRepository;
	        this.passwordEncoder = passwordEncoder;
	        this.jwtService = jwtService;
	        this.refreshTokenService = refreshTokenService;
	    }

	@Override
	public LoginResponse login(LoginRequest request) {
		 Organization organization = organizationRepository
	                .findByCode(request.getOrganizationCode())
	                .orElseThrow(() ->
	                        new BadCredentialsException(
	                                "Invalid organization or credentials"
	                        ));

	        User user = userRepository
	                .findByEmailAndOrganization_OrganizationId(
	                        request.getEmail(),
	                        organization.getOrganizationId()
	                )
	                .orElseThrow(() ->
	                        new BadCredentialsException(
	                                "Invalid organization or credentials"
	                        ));

	        if (!Boolean.TRUE.equals(user.getActive())) {
	            throw new BadCredentialsException(
	                    "User account is inactive"
	            );
	        }

	        boolean passwordMatches = passwordEncoder.matches(
	                request.getPassword(),
	                user.getPassword()
	        );

	        if (!passwordMatches) {
	            throw new BadCredentialsException(
	                    "Invalid organization or credentials"
	            );
	        }

	        String accessToken = jwtService.generateAccessToken(user);
	        // Generate refresh token
	        RefreshToken refreshToken =
	                refreshTokenService.createRefreshToken(user);
	        return new LoginResponse(
	                accessToken,
	                refreshToken.getToken(),
	                "Bearer",
	                jwtService.getAccessTokenExpiration() / 1000,
	                user.getUserId(),
	                user.getName(),
	                user.getEmail(),
	                user.getRole().name(),
	                organization.getOrganizationId()
	        );
	}
	@Override
	public LoginResponse refreshAccessToken(String refreshToken) {
        RefreshToken storedToken =
                refreshTokenService.verifyRefreshToken(
                        refreshToken
                );

        User user = storedToken.getUser();

        String newAccessToken =
                jwtService.generateAccessToken(user);

        return new LoginResponse(
                newAccessToken,
                storedToken.getToken(),
                "Bearer",
                jwtService.getAccessTokenExpiration() / 1000,
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getOrganization().getOrganizationId()
        );}
	@Override
	public void logout(String refreshToken) {
		 refreshTokenService.revokeToken(refreshToken);
		
	}

}
