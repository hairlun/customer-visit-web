package com.jude.dao;

import com.jude.entity.InteractiveItem;
import com.jude.util.PagingHelper;
import com.jude.util.PagingSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("interactiveItemDao")
public class InteractiveItemDaoImpl implements InteractiveItemDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addInteractiveItem(InteractiveItem interactiveItem) {
		this.jdbcTemplate.update(
				"insert into interactive_item(name) values(?)",
				new Object[] { interactiveItem.getName() });
	}

	public void deleteInteractiveItems(String ids) {
		this.jdbcTemplate.update("delete from interactive_item where id in (" + ids + ")");
	}

	public PagingSet<InteractiveItem> getInteractiveItems(int start, int pageSize,
			String sort, String dir) {
		String sql;
		if (sort != null && !sort.equals("") && dir != null && !dir.equals("")) {
			sql = "select id, name from interactive_item order by " + sort + " " + dir;
		} else {
			sql = "select id, name from interactive_item";
		}
		PagingHelper h = new PagingHelper(this.jdbcTemplate);
		h.setInParameters(sql, start, pageSize);
		PagingSet<InteractiveItem> pageSet = h.handle(new InteractiveItemRowMapper());
		return pageSet;
	}

	public InteractiveItem getInteractiveItem(String name) {
		List<InteractiveItem> list = this.jdbcTemplate.query(
				"select id, name from interactive_item where name = ?",
				new Object[] { name }, new InteractiveItemRowMapper());

		if ((list != null) && (list.size() > 0)) {
			return (InteractiveItem) list.get(0);
		}
		return null;
	}

	public InteractiveItem getInteractiveItem(long id) {
		List<InteractiveItem> list = this.jdbcTemplate.query(
				"select id, name from interactive_item where id = ?",
				new Object[] { Long.valueOf(id) }, new InteractiveItemRowMapper());

		if ((list != null) && (list.size() > 0)) {
			return (InteractiveItem) list.get(0);
		}
		return null;
	}

	public void updateInteractiveItem(InteractiveItem interactiveItem) {
		this.jdbcTemplate.update(
				"update interactive_item set name = ? where id = ?",
				new Object[] { interactiveItem.getName(), interactiveItem.getId() });
	}

	public boolean nameExist(String name) {
		int i = this.jdbcTemplate.queryForInt(
				"select count(1) from interactive_item where name = ?",
				new Object[] { name });

		return i >= 1;
	}

	public static class InteractiveItemRowMapper implements RowMapper<InteractiveItem> {
		public InteractiveItem mapRow(ResultSet rs, int rowNum) throws SQLException {
			InteractiveItem interactiveItem = new InteractiveItem();
			interactiveItem.setId(Long.valueOf(rs.getLong("id")));
			interactiveItem.setName(rs.getString("name"));
			return interactiveItem;
		}
	}
}