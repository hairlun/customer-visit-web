package com.jude.service;

import com.jude.dao.CustomerManagerDao;
import com.jude.entity.CustomerManager;
import com.jude.util.PagingSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("customerManagerService")
public class CustomerManagerServiceImpl implements CustomerManagerService {
	
	private static CustomerManager loginUser;

	@Autowired
	private CustomerManagerDao customerManagerDao;

	public void addCustomerManager(CustomerManager cutomerManager) {
		this.customerManagerDao.addCustomerManager(cutomerManager);
	}

	public void deleteCustomerManagers(String ids) {
		this.customerManagerDao.deleteCustomerManagers(ids);
	}

	public CustomerManager getCustomerManager(String username) {
		return this.customerManagerDao.getCustomerManager(username);
	}

	public PagingSet<CustomerManager> getCustomerManagers(int start, int pageSize,
			String sort, String dir, String where) {
		return this.customerManagerDao.getCustomerMangers(start, pageSize, sort, dir, where);
	}

	public boolean usernameExist(String username) {
		return this.customerManagerDao.usernameExist(username);
	}

	public void updateCustomerManager(CustomerManager manager) {
		this.customerManagerDao.updateCustomerManager(manager);
	}

	public CustomerManager getCustomerManager(long id) {
		return this.customerManagerDao.getCustomerManager(id);
	}

	public static CustomerManager getLoginUser() {
		return loginUser;
	}

	public static void setLoginUser(CustomerManager loginUser) {
		CustomerManagerServiceImpl.loginUser = loginUser;
	}
}