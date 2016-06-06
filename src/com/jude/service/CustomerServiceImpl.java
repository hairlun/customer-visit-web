package com.jude.service;

import com.jude.dao.CustomerDao;
import com.jude.entity.Customer;
import com.jude.util.PagingSet;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerDao customerDao;

	public void addCustomer(Customer customer) {
		this.customerDao.addCustomer(customer);
	}

	public void deleteCustomers(String ids) {
		this.customerDao.deleteCustomers(ids);
	}

	public Customer getCustomer(int id) {
		return this.customerDao.getCustomer(id);
	}

	public PagingSet<Customer> getCustomers(int start, int pageSize) {
		return this.customerDao.getCustomers(start, pageSize);
	}

	public PagingSet<Customer> getCustomers(int start, int pageSize, int groupId,
			String sort, String dir) {
		if (groupId == -1) {
			return this.customerDao.getCustomers(start, pageSize, sort, dir);
		} else {
			return this.customerDao.getCustomers(start, pageSize, groupId, sort, dir);
		}
	}

	public void updateCustomerManager(Customer customer) {
		this.customerDao.updateCustomerManager(customer);
	}

	public void updateLastVisitTime(Date time, Long id) {
		this.customerDao.updateLastVisitTime(time, id);
	}

	public void updateGps(String gps, Long id) {
		this.customerDao.updateGps(gps, id);
	}

	public boolean checkExist(String number) {
		return this.customerDao.checkExist(number);
	}

	public Customer getCustomerByNumber(String number) {
		return this.customerDao.getCustomerByNumber(number);
	}

	public void update(Customer customer) {
		this.customerDao.update(customer);
	}

	public List<Customer> getCustomersByManagerIds(String ids) {
		return this.customerDao.getCustomersByManagerIds(ids);
	}

	public List<Customer> getCustomersByGroupIds(String ids) {
		return this.customerDao.getCustomersByGroupIds(ids);
	}

	public List<Customer> getCustomersByIds(String ids) {
		return this.customerDao.getCustomersByIds(ids);
	}

	public boolean checkSign(String code, String serviceId) {
		return this.customerDao.checkSign(code, serviceId);
	}
}