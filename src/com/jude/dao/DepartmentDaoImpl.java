package com.jude.dao;

import com.jude.entity.Department;
import com.jude.util.PagingHelper;
import com.jude.util.PagingSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("departmentDao")
public class DepartmentDaoImpl implements DepartmentDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addDepartment(Department department) {
		this.jdbcTemplate.update(
				"insert into department(name) values(?)",
				new Object[] { department.getName() });
	}

	public void deleteDepartments(String ids) {
		this.jdbcTemplate.update("delete from department where id in (" + ids + ")");
	}

	public PagingSet<Department> getDepartments(int start, int pageSize,
			String sort, String dir) {
		String sql;
		if (sort != null && !sort.equals("") && dir != null && !dir.equals("")) {
			sql = "select id, name from department order by " + sort + " " + dir;
		} else {
			sql = "select id, name from department";
		}
		PagingHelper h = new PagingHelper(this.jdbcTemplate);
		h.setInParameters(sql, start, pageSize);
		PagingSet<Department> pageSet = h.handle(new DepartmentRowMapper());
		return pageSet;
	}

	public Department getDepartment(String name) {
		List<Department> list = this.jdbcTemplate.query(
				"select id, name from department where name = ?",
				new Object[] { name }, new DepartmentRowMapper());

		if ((list != null) && (list.size() > 0)) {
			return (Department) list.get(0);
		}
		return null;
	}

	public Department getDepartment(long id) {
		List<Department> list = this.jdbcTemplate.query(
				"select id, name from department where id = ?",
				new Object[] { Long.valueOf(id) }, new DepartmentRowMapper());

		if ((list != null) && (list.size() > 0)) {
			return (Department) list.get(0);
		}
		return null;
	}

	public void updateDepartment(Department department) {
		this.jdbcTemplate.update(
				"update department set name = ? where id = ?",
				new Object[] { department.getName(), department.getId() });
	}

	public boolean nameExist(String name) {
		int i = this.jdbcTemplate.queryForInt(
				"select count(1) from department where name = ?",
				new Object[] { name });

		return i >= 1;
	}

	public void joinDepartment(long managerId, long departmentId) {
		this.jdbcTemplate.update("insert into department_manager_relation(manager_id, department_id) values(?, ?)",
				new Object[] { Long.valueOf(managerId), Long.valueOf(departmentId) });
	}

	public void exitDepartment(long managerId, long departmentId) {
		this.jdbcTemplate.update("delete from department_manager_relation where manager_id = ? and department_id = ?",
				new Object[] { Long.valueOf(managerId), Long.valueOf(departmentId) });
	}

	public static class DepartmentRowMapper implements RowMapper<Department> {
		public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
			Department department = new Department();
			department.setId(Long.valueOf(rs.getLong("id")));
			department.setName(rs.getString("name"));
			return department;
		}
	}
}