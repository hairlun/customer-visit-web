package com.jude.dao;

import com.jude.entity.CustomerGroup;
import com.jude.util.PagingHelper;
import com.jude.util.PagingSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("customerGroupDao")
public class CustomerGroupDaoImpl implements CustomerGroupDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addCustomerGroup(CustomerGroup customerGroup) {
		this.jdbcTemplate.update(
				"insert into customer_group(name) values(?)",
				new Object[] { customerGroup.getName() });
	}

	public void deleteCustomerGroups(String ids) {
		this.jdbcTemplate.update("delete from customer_group where id in (" + ids + ")");
	}

	public PagingSet<CustomerGroup> getCustomerGroups(int start, int pageSize,
			String sort, String dir) {
		String sql;
		if (sort != null && !sort.equals("") && dir != null && !dir.equals("")) {
			sql = "select id, name from customer_group order by " + sort + " " + dir;
		} else {
			sql = "select id, name from customer_group";
		}
		PagingHelper h = new PagingHelper(this.jdbcTemplate);
		h.setInParameters(sql, start, pageSize);
		PagingSet<CustomerGroup> pageSet = h.handle(new CustomerGroupRowMapper());
		return pageSet;
	}

	public CustomerGroup getCustomerGroup(String name) {
		List<CustomerGroup> list = this.jdbcTemplate.query(
				"select id, name from customer_group where name = ?",
				new Object[] { name }, new CustomerGroupRowMapper());

		if ((list != null) && (list.size() > 0)) {
			return (CustomerGroup) list.get(0);
		}
		return null;
	}

	public CustomerGroup getCustomerGroup(int id) {
		List<CustomerGroup> list = this.jdbcTemplate.query(
				"select id, name from customer_group where id = ?",
				new Object[] { Integer.valueOf(id) }, new CustomerGroupRowMapper());

		if ((list != null) && (list.size() > 0)) {
			return (CustomerGroup) list.get(0);
		}
		return null;
	}

	public void updateCustomerGroup(CustomerGroup group) {
		this.jdbcTemplate.update(
				"update customer_group set name = ? where id = ?",
				new Object[] { group.getName(), group.getId() });
	}

	public boolean nameExist(String name) {
		int i = this.jdbcTemplate.queryForInt(
				"select count(1) from customer_group where name = ?",
				new Object[] { name });

		return i >= 1;
	}

	public void joinGroup(long customerId, long groupId) {
		this.jdbcTemplate.update("insert into customer_group_relation(customer_id, group_id) values(?, ?)",
				new Object[] { customerId, groupId });
	}

	public void exitGroup(long customerId, long groupId) {
		this.jdbcTemplate.update("delete from customer_group_relation where customer_id = ? and group_id = ?",
				new Object[] { customerId, groupId });
	}

	public static class CustomerGroupRowMapper implements RowMapper<CustomerGroup> {
		public CustomerGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
			CustomerGroup group = new CustomerGroup();
			group.setId(Long.valueOf(rs.getLong("id")));
			group.setName(rs.getString("name"));
			return group;
		}
	}
}