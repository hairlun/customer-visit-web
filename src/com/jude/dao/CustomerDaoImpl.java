package com.jude.dao;

import com.jude.entity.Customer;
import com.jude.entity.CustomerGroup;
import com.jude.entity.CustomerManager;
import com.jude.util.PagingHelper;
import com.jude.util.PagingSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("customerDao")
public class CustomerDaoImpl implements CustomerDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addCustomer(Customer customer) {
		String sql = "insert into customer(number, name, sell_number, store_name, level, phone_number, backup_number, address, manager_id, group_id, order_type, gps, last_visit_time) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		this.jdbcTemplate.update(
				sql,
				new Object[] {
						customer.getNumber(),
						customer.getName(),
						customer.getSellNumber(),
						customer.getStoreName(),
						customer.getLevel(),
						customer.getPhoneNumber(),
						customer.getBackupNumber(),
						customer.getAddress(),
						Long.valueOf((customer.getCustomerManager() == null) ? 0L : customer
								.getCustomerManager().getId().longValue()),
						Long.valueOf((customer.getCustomerGroup() == null) ? 0L : customer
								.getCustomerGroup().getId().longValue()),
						customer.getOrderType(), customer.getGps(), customer.getLastVisitTime() });
	}

	public void deleteCustomers(String ids) {
		this.jdbcTemplate.update("delete from customer where id in (" + ids + ")");
	}

	public PagingSet<Customer> getCustomers(int start, int pageSize) {
		String sql = "select c.id, c.number, c.name, c.sell_number, c.store_name, c.level, c.phone_number, c.backup_number, c.address, c.manager_id, c.group_id, c.order_type, c.gps, c.last_visit_time, m.name as mname, m.username as musername, m.password as mpassword, g.name as gname from (customer c left join customer_manager m on c.manager_id = m.id) left join customer_group g on c.group_id = g.id";
		PagingHelper h = new PagingHelper(this.jdbcTemplate);
		h.setInParameters(sql, start, pageSize);
		PagingSet<Customer> pageSet = h.handle(new CustomerRowMapper());
		return pageSet;
	}

	public PagingSet<Customer> getCustomers(int start, int pageSize, String sort, String dir) {
		String sql = "select c.id, c.number, c.name, c.sell_number, c.store_name, c.level, c.phone_number, c.backup_number, c.address, c.manager_id, c.group_id, c.order_type, c.gps, c.last_visit_time, m.name as mname, m.username as musername, m.password as mpassword, g.name as gname from (customer c left join customer_manager m on c.manager_id = m.id) left join customer_group g on c.group_id = g.id";
		if (sort != null && !sort.equals("") && dir != null && !dir.equals("")) {
			sql += " order by " + sort + " " + dir;
		}
		PagingHelper h = new PagingHelper(this.jdbcTemplate);
		h.setInParameters(sql, start, pageSize);
		PagingSet<Customer> pageSet = h.handle(new CustomerRowMapper());
		return pageSet;
	}

	public PagingSet<Customer> getCustomers(int start, int pageSize, int groupId,
			String sort, String dir) {
		String sql = "select c.id, c.number, c.name, c.sell_number, c.store_name, c.level, c.phone_number, c.backup_number, c.address, c.manager_id, c.group_id, c.order_type, c.gps, c.last_visit_time, m.name as mname, m.username as musername, m.password as mpassword, g.name as gname from (customer c left join customer_manager m on c.manager_id = m.id) left join customer_group g on c.group_id = g.id where g.id = " + groupId;
		if (sort != null && !sort.equals("") && dir != null && !dir.equals("")) {
			sql += " order by " + sort + " " + dir;
		}
		PagingHelper h = new PagingHelper(this.jdbcTemplate);
		h.setInParameters(sql, start, pageSize);
		PagingSet<Customer> pageSet = h.handle(new CustomerRowMapper());
		return pageSet;
	}

	public Customer getCustomer(int id) {
		List<Customer> customers = this.jdbcTemplate
				.query("select c.id, c.number, c.name, c.sell_number, c.store_name, c.level, c.phone_number, c.backup_number, c.address, c.manager_id, c.group_id, c.order_type, c.gps, c.last_visit_time, m.name as mname, m.username as musername, m.password as mpassword, g.name as gname from (customer c left join customer_manager m on c.manager_id = m.id) left join customer_group g on c.group_id = g.id where c.id = "
						+ id, new CustomerRowMapper());
		if ((customers != null) && (customers.size() > 0)) {
			return (Customer) customers.get(0);
		}
		return null;
	}

	public boolean checkExist(String number) {
		int i = this.jdbcTemplate.queryForInt("select count(1) from customer where number = ?",
				new Object[] { number });

		return i >= 1;
	}

	public void updateCustomerGroup(Customer customer) {
		this.jdbcTemplate.update("update customer set group_id = "
				+ customer.getCustomerGroup().getId() + " where id = " + customer.getId());
	}

	public void updateCustomerManager(Customer customer) {
		this.jdbcTemplate.update("update customer set manager_id = "
				+ customer.getCustomerManager().getId() + " where id = " + customer.getId());
	}

	public void update(Customer customer) {
		this.jdbcTemplate
				.update("update customer set name = ?, number = ?, sell_number = ?, store_name = ?, level = ?, phone_number = ?, backup_number = ?, address = ?, order_type = ?, gps = ? where id = ?",
						new Object[] { customer.getName(), customer.getNumber(),
								customer.getSellNumber(), customer.getStoreName(),
								customer.getLevel(), customer.getPhoneNumber(),
								customer.getBackupNumber(), customer.getAddress(),
								customer.getOrderType(), customer.getGps(), customer.getId() });
	}

	public void updateLastVisitTime(Date time, Long id) {
		this.jdbcTemplate.update("update customer set last_visit_time = ? where id = ?",
				new Object[] { time, id });
	}

	public void updateGps(String gps, Long id) {
		this.jdbcTemplate.update("update customer set gps = ? where id = ?",
				new Object[] { gps, id });
	}

	public Customer getCustomerByNumber(String number) {
		String sql = "select c.id, c.number, c.name, c.sell_number, c.store_name, c.level, c.phone_number, c.backup_number, c.address, c.manager_id, c.group_id, c.order_type, c.gps, c.last_visit_time, m.name as mname, m.username as musername, m.password as mpassword, g.name as gname from (customer c left join customer_manager m on c.manager_id = m.id) left join customer_group g on c.group_id = g.id where c.number = '"
				+ number + "'";
		PagingHelper h = new PagingHelper(this.jdbcTemplate);
		h.setInParameters(sql, 0, 10);
		PagingSet<Customer> pageSet = h.handle(new CustomerRowMapper());
		if ((pageSet == null) || (pageSet.getList() == null) || (pageSet.getList().size() == 0)) {
			return null;
		}
		return (Customer) pageSet.getList().get(0);
	}

	public List<Customer> getCustomersByManagerIds(String ids) {
		String sql = "select c.id, c.number, c.name, c.sell_number, c.store_name, c.level, c.phone_number, c.backup_number, c.address, c.manager_id, c.group_id, c.order_type, c.gps, c.last_visit_time, m.name as mname, m.username as musername, m.password as mpassword, g.name as gname from (customer c left join customer_manager m on c.manager_id = m.id) left join customer_group g on c.group_id = g.id where c.manager_id in ("
				+ ids + ")";
		return this.jdbcTemplate.query(sql, new CustomerRowMapper());
	}

	public List<Customer> getCustomersByIds(String ids) {
		String sql = "select c.id, c.number, c.name, c.sell_number, c.store_name, c.level, c.phone_number, c.backup_number, c.address, c.manager_id, c.group_id, c.order_type, c.gps, c.last_visit_time, m.name as mname, m.username as musername, m.password as mpassword, g.name as gname from (customer c left join customer_manager m on c.manager_id = m.id) left join customer_group g on c.group_id = g.id where c.id in ("
				+ ids + ")";
		return this.jdbcTemplate.query(sql, new CustomerRowMapper());
	}

	public boolean checkSign(String code, String serviceId) {
		String sql = "select count(1) from task t left join customer c on t.customer = c.id where c.sell_number = '"
				+ code + "' and t.id = " + serviceId;
		int k = this.jdbcTemplate.queryForInt(sql);
		return k > 0;
	}

	public static class CustomerRowMapper implements RowMapper<Customer> {
		public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Customer customer = new Customer();
			customer.setId(Long.valueOf(rs.getLong("id")));
			customer.setNumber(rs.getString("number"));
			customer.setName(rs.getString("name"));
			customer.setSellNumber(rs.getString("sell_number"));
			customer.setStoreName(rs.getString("store_name"));
			customer.setLevel(rs.getString("level"));
			customer.setPhoneNumber(rs.getString("phone_number"));
			customer.setBackupNumber(rs.getString("backup_number"));
			customer.setAddress(rs.getString("address"));
			customer.setOrderType(rs.getString("order_type"));
			customer.setGps(rs.getString("gps"));
			customer.setLastVisitTime(rs.getTimestamp("last_visit_time"));
			try {
				String musername = rs.getString("musername");
				if ((musername != null) && (musername.length() > 0)) {
					CustomerManager customerManager = new CustomerManager();
					customerManager.setId(Long.valueOf(rs.getLong("manager_id")));
					customerManager.setUsername(musername);
					customerManager.setName(rs.getString("mname"));
					customerManager.setPassword(rs.getString("mpassword"));
					customer.setCustomerManager(customerManager);
				}
				String gname = rs.getString("gname");
				if (gname != null && gname.length() > 0) {
					CustomerGroup customerGroup = new CustomerGroup();
					customerGroup.setId(Long.valueOf(rs.getLong("group_id")));
					customerGroup.setName(rs.getString("gname"));
					customer.setCustomerGroup(customerGroup);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return customer;
		}
	}
}