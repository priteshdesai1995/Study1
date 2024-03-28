package com.base.api.enums;

public enum UserRole {

	ROLE_SUPERADMIN("ROLE_SUPERADMIN"), ROLE_ADMIN("ROLE_ADMIN"), ROLE_USER("ROLE_USER");

	private String role;

	private UserRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

}
