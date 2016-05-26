package com.jude.service;

import com.jude.entity.User;
import com.jude.util.PagingSet;
import java.util.List;

public abstract interface UserService {
	public abstract boolean isUserExist(String paramString);

	public abstract User getUser(String paramString);

	public abstract void addUser(User paramUser);

	public abstract void deleteUser(int paramInt);

	public abstract List<User> getUsers();

	public abstract PagingSet<User> getUsers(int paramInt1, int paramInt2);
}