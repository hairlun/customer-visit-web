package com.jude.dao;

import com.jude.entity.CustomerGroup;
import com.jude.util.PagingSet;

public abstract interface CustomerGroupDao {
	public abstract void addCustomerGroup(CustomerGroup paramCustomerGroup);

	public abstract void deleteCustomerGroups(String paramString);

	public abstract CustomerGroup getCustomerGroup(String paramString);

	public abstract boolean nameExist(String paramString);

	public abstract void updateCustomerGroup(CustomerGroup paramCustomerGroup);

	public abstract CustomerGroup getCustomerGroup(int paramInt);

	public abstract PagingSet<CustomerGroup> getCustomerGroups(int paramInt1, int paramInt2,
			String paramString3, String paramString4);
}