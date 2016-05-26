package com.jude.dao;

import com.jude.entity.ModelSwitch;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("switchDao")
public class SwitchDaoImpl implements SwitchDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void update(ModelSwitch modelSwitch) {
		this.jdbcTemplate.update(
				"update model_switch set qrcode_switch = ?, timestamp_switch = ? where id = 1",
				new Object[] { modelSwitch.getQrcode(), modelSwitch.getTimestamp() });
	}

	public ModelSwitch query() {
		return (ModelSwitch) this.jdbcTemplate.queryForObject(
				"select qrcode_switch, timestamp_switch from model_switch where id = 1",
				new RowMapper() {
					public ModelSwitch mapRow(ResultSet rs, int rowNum) throws SQLException {
						ModelSwitch ms = new ModelSwitch();
						ms.setQrcode(rs.getString("qrcode_switch"));
						ms.setTimestamp(rs.getString("timestamp_switch"));
						return ms;
					}
				});
	}
}