package com.jude.dao;

import com.jude.entity.CustomerManager;
import com.jude.util.PagingSet;

public abstract interface CustomerManagerDao {
	public abstract void addCustomerManager(CustomerManager paramCustomerManager);

	public abstract void deleteCustomerManagers(String paramString);

	public abstract CustomerManager getCustomerManager(String paramString);

	public abstract boolean usernameExist(String paramString);

	public abstract void updateCustomerManager(CustomerManager paramCustomerManager);

	public abstract CustomerManager getCustomerManager(int paramInt);

	public abstract PagingSet<CustomerManager> getCustomerMangers(int paramInt1, int paramInt2,
			String paramString3, String paramString4);
}