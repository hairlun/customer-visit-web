package com.jude.service;

import com.jude.dao.CustomerGroupDao;
import com.jude.entity.CustomerGroup;
import com.jude.util.PagingSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("customerGroupService")
public class CustomerGroupServiceImpl implements CustomerGroupService {

	@Autowired
	private CustomerGroupDao customerGroupDao;

	public void addCustomerGroup(CustomerGroup cutomerGroup) {
		this.customerGroupDao.addCustomerGroup(cutomerGroup);
	}

	public void deleteCustomerGroups(String ids) {
		this.customerGroupDao.deleteCustomerGroups(ids);
	}

	public CustomerGroup getCustomerGroup(String username) {
		return this.customerGroupDao.getCustomerGroup(username);
	}

	public PagingSet<CustomerGroup> getCustomerGroups(int start, int pageSize,
			String sort, String dir) {
		return this.customerGroupDao.getCustomerGroups(start, pageSize, sort, dir);
	}

	public boolean nameExist(String username) {
		return this.customerGroupDao.nameExist(username);
	}

	public void updateCustomerGroup(CustomerGroup manager) {
		this.customerGroupDao.updateCustomerGroup(manager);
	}

	public CustomerGroup getCustomerGroup(int id) {
		return this.customerGroupDao.getCustomerGroup(id);
	}

	public void joinGroup(long customerId, long groupId) {
		this.customerGroupDao.joinGroup(customerId, groupId);
	}

	public void exitGroup(long customerId, long groupId) {
		this.customerGroupDao.exitGroup(customerId, groupId);
	}
}