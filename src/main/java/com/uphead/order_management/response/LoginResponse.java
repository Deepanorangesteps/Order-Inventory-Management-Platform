package com.uphead.order_management.response;

public class LoginResponse {
	 private String accessToken;
	    private String refreshToken;
	    private String tokenType;
	    private Long expiresIn;
	    private Long userId;
	    private String name;
	    private String email;
	    private String role;
	    private Long organizationId;

	    public LoginResponse() {
	    }


		public LoginResponse(String accessToken, String refreshToken, String tokenType, Long expiresIn, Long userId,
				String name, String email, String role, Long organizationId) {
			super();
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
			this.tokenType = tokenType;
			this.expiresIn = expiresIn;
			this.userId = userId;
			this.name = name;
			this.email = email;
			this.role = role;
			this.organizationId = organizationId;
		}


		public String getAccessToken() {
			return accessToken;
		}

		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}

		public String getRefreshToken() {
			return refreshToken;
		}

		public void setRefreshToken(String refreshToken) {
			this.refreshToken = refreshToken;
		}

		public String getTokenType() {
			return tokenType;
		}

		public void setTokenType(String tokenType) {
			this.tokenType = tokenType;
		}

		public Long getExpiresIn() {
			return expiresIn;
		}

		public void setExpiresIn(Long expiresIn) {
			this.expiresIn = expiresIn;
		}

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public Long getOrganizationId() {
			return organizationId;
		}

		public void setOrganizationId(Long organizationId) {
			this.organizationId = organizationId;
		}
	    
	    
	    

}
