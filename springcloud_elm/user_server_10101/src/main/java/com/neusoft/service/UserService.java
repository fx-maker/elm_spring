package com.neusoft.service;

import java.util.List;
import com.neusoft.po.User;

public interface UserService {

	public User getUserByIdByPass(User user);
	public int getUserById(String userId);
	public int saveUser(User user);
	public List<User> listAllUsers();
	public int updateUserType(String userId, String userType);
}
