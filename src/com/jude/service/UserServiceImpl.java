package com.jude.service;

import com.jude.dao.UserDao;
import com.jude.entity.User;
import com.jude.util.PagingSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	private static User loginUser;

	@Autowired
	private UserDao userDao;

	public boolean isUserExist(String name) {
		return this.userDao.isUserExist(name);
	}

	public User getUser(String name) {
		return this.userDao.getUser(name);
	}

	public void addUser(User user) {
		this.userDao.addUser(user);
	}

	public void deleteUser(int id) {
		this.userDao.deleteUser(id);
	}

	public List<User> getUsers() {
		return this.userDao.getUsers();
	}

	public PagingSet<User> getUsers(int start, int pageSize) {
		return this.userDao.getUsers(start, pageSize);
	}

	public static User getLoginUser() {
		return loginUser;
	}

	public static void setLoginUser(User loginUser) {
		UserServiceImpl.loginUser = loginUser;
	}
}