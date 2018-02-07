package com.sss.access;

import com.sss.domain.User;

/**
 * @author v_shishusheng
 */
public class UserContext {
	
	private static ThreadLocal<User> userHolder = new ThreadLocal<>();

	public static void setUser(User user) {
		userHolder.set(user);
	}

	public static User getUser() {
		return userHolder.get();
	}

}
