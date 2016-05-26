package com.jude.dao;

import com.jude.entity.User;
import com.jude.util.PagingHelper;
import com.jude.util.PagingSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addUser(User user) {
		StringBuffer sb = new StringBuffer(
				"insert into user(username, password, fullname) values(?, ?, ?)");

		this.jdbcTemplate.update(sb.toString(),
				new Object[] { user.getUsername(), user.getPassword(), user.getFullname() });
	}

	public boolean isUserExist(String username) {
		User user = getUser(username);
		return user != null;
	}

	public List<User> getUsers() {
		StringBuffer sb = new StringBuffer("select id, username, password, fullname from user");
		List users = this.jdbcTemplate.query(sb.toString(), new UserRowMapper());
		return users;
	}

	public void deleteUser(int id) {
		this.jdbcTemplate.update("delete from user where id = ?",
				new Object[] { Integer.valueOf(id) });
	}

	public User getUser(String username) {
		StringBuffer sb = new StringBuffer(
				"select id, username, password, fullname from user where username = ?");
		List users = this.jdbcTemplate.query(sb.toString(), new Object[] { username },
				new UserRowMapper());
		if ((users == null) || (users.size() == 0)) {
			return null;
		}
		return (User) users.get(0);
	}

	public PagingSet<User> getUsers(int start, int pageSize) {
		PagingHelper h = new PagingHelper(this.jdbcTemplate);
		StringBuffer sb = new StringBuffer("select id, username, password, fullname from user");
		h.setInParameters(sb.toString(), start, pageSize);
		PagingSet pageSet = h.handle(new UserRowMapper());
		return pageSet;
	}

	public static class UserRowMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(Long.valueOf(rs.getLong("id")));
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("password"));
			user.setFullname(rs.getString("fullname"));
			return user;
		}
	}
}