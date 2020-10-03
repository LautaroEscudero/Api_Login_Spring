package com.example.representation;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationRequest implements Serializable {

		private static final long serialVersionUID = 1L;

		@JsonProperty("user")
		private String userName;

		private String password;

		public AuthorizationRequest() {
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
		
		
	}
