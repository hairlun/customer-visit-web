package com.jude.dao;

import com.jude.dao.CustomerDaoImpl.CustomerRowMapper;
import com.jude.entity.Customer;
import com.jude.entity.CustomerManager;
import com.jude.util.PagingHelper;
import com.jude.util.PagingSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("customerManagerDao")
public class CustomerManagerDaoImpl implements CustomerManagerDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addCustomerManager(CustomerManager customerManager) {
		this.jdbcTemplate.update(
				"insert into customer_manager(name, username, password) values(?, ?, ?)",
				new Object[] { customerManager.getName(), customerManager.getUsername(),
						customerManager.getPassword() });
	}

	public void deleteCustomerManagers(String ids) {
		this.jdbcTemplate.update("delete from customer_manager where id in (" + ids + ")");
	}

	public PagingSet<CustomerManager> getCustomerMangers(int start, int pageSize,
			String sort, String dir) {
		String sql;
		if (sort != null && !sort.equals("") && dir != null && !dir.equals("")) {
			sql = "select id, name, username, password from customer_manager order by "
					+ sort + " " + dir;
		} else {
			sql = "select id, name, username, password from customer_manager";
		}
		PagingHelper h = new PagingHelper(this.jdbcTemplate);
		h.setInParameters(sql, start, pageSize);
		PagingSet pageSet = h.handle(new CustomerManagerRowMapper());
		return pageSet;
	}

	public CustomerManager getCustomerManager(String username) {
		List list = this.jdbcTemplate.query(
				"select id, name, username, password from customer_manager where username = ?",
				new Object[] { username }, new CustomerManagerRowMapper());

		if ((list != null) && (list.size() > 0)) {
			return (CustomerManager) list.get(0);
		}
		return null;
	}

	public CustomerManager getCustomerManager(long id) {
		List list = this.jdbcTemplate.query(
				"select id, name, username, password from customer_manager where id = ?",
				new Object[] { Long.valueOf(id) }, new CustomerManagerRowMapper());

		if ((list != null) && (list.size() > 0)) {
			return (CustomerManager) list.get(0);
		}
		return null;
	}

	public List<CustomerManager> getCustomerManagersByDepartmentIds(String ids) {
		String sql = "select distinct m.id, m.name, m.username, m.password from customer_manager m left join department_manager_relation dmr on m.id = dmr.manager_id where dmr.department_id in ("
				+ ids + ")";
		return this.jdbcTemplate.query(sql, new CustomerManagerRowMapper());
	}

	public void updateCustomerManager(CustomerManager manager) {
		this.jdbcTemplate.update(
				"update customer_manager set name = ?, password = ?, username = ? where id = ?",
				new Object[] { manager.getName(), manager.getPassword(), manager.getUsername(),
						manager.getId() });
	}

	public boolean usernameExist(String username) {
		int i = this.jdbcTemplate.queryForInt(
				"select count(1) from customer_manager where username = ?",
				new Object[] { username });

		return i >= 1;
	}

	public static class CustomerManagerRowMapper implements RowMapper<CustomerManager> {
		public CustomerManager mapRow(ResultSet rs, int rowNum) throws SQLException {
			CustomerManager manager = new CustomerManager();
			manager.setId(Long.valueOf(rs.getLong("id")));
			manager.setName(rs.getString("name"));
			manager.setPassword(rs.getString("password"));
			manager.setUsername(rs.getString("username"));
			return manager;
		}
	}
}