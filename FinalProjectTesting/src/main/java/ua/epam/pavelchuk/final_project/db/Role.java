package ua.epam.pavelchuk.final_project.db;

import ua.epam.pavelchuk.final_project.db.entity.User;

/**
 * Role entity.
 * 
 * @author 
 * 
 */

public enum Role {
	ADMIN, CLIENT;
	
	public static Role getRole(User user) {
		int roleId = user.getRoleId();
		return Role.values()[roleId];
	}
	
	public String getName() {
		return name().toLowerCase();
	}
	
}

