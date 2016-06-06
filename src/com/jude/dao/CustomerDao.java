package com.jude.dao;

import com.jude.entity.Customer;
import com.jude.util.PagingSet;

import java.util.Date;
import java.util.List;

public abstract interface CustomerDao {
	public abstract void addCustomer(Customer paramCustomer);

	public abstract void deleteCustomers(String paramString);

	public abstract Customer getCustomer(int paramInt);

	public abstract PagingSet<Customer> getCustomers(int paramInt1, int paramInt2);

	public abstract PagingSet<Customer> getCustomers(int paramInt1, int paramInt2,
			String paramString3, String paramString4);

	public abstract PagingSet<Customer> getCustomers(int paramInt1, int paramInt2, int paramInt3,
			String paramString4, String paramString5);

	public abstract void updateCustomerManager(Customer paramCustomer);
	
	public abstract void updateLastVisitTime(Date time, Long id);
	
	public abstract void updateGps(String gps, Long id);

	public abstract void update(Customer paramCustomer);

	public abstract boolean checkExist(String paramString);

	public abstract Customer getCustomerByNumber(String paramString);

	public abstract List<Customer> getCustomersByManagerIds(String paramString);

	public abstract List<Customer> getCustomersByGroupIds(String paramString);
	
	public abstract List<Customer> getCustomersByIds(String paramString);

	public abstract boolean checkSign(String paramString1, String paramString2);
}