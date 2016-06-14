package com.jude.dao;

import com.jude.entity.Customer;
import com.jude.entity.CustomerManager;
import com.jude.entity.RecordDetail;
import com.jude.entity.VisitRecord;
import com.jude.util.PagingHelper;
import com.jude.util.PagingSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("visitRecordDao")
public class VisitRecordDaoImpl implements VisitRecordDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addVisitRecord(VisitRecord visitRecord, List<RecordDetail> details) {
		this.jdbcTemplate
				.update("insert into visit_record(id, customer, customer_manager, type, visit_time, content, city, gps, task_id, images, result_code) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						new Object[] { visitRecord.getId(), visitRecord.getCustomer().getId(),
								visitRecord.getCustomerManager().getId(),
								Integer.valueOf(visitRecord.getType()), visitRecord.getVisitTime(),
								visitRecord.getContent(), visitRecord.getCity(),
								visitRecord.getGps(), Long.valueOf(visitRecord.getTaskId()),
								Integer.valueOf(visitRecord.getImages()),
								Integer.valueOf(visitRecord.getResultCode()) });

		if (details.size() > 0) {
			StringBuffer sb = new StringBuffer("insert into record_detail(record_id, content) values");
			for (RecordDetail detail : details) {
				sb.append("(").append(visitRecord.getId()).append(", '").append(detail.getContent())
						.append("'),");
			}
			this.jdbcTemplate.update(sb.substring(0, sb.length() - 1).toString());
		}
	}

	public void deleteVisitRecord(String ids) {
		this.jdbcTemplate.update("delete from visit_record where id in (" + ids + ")");
		this.jdbcTemplate.update("delete from record_detail where record_id in (" + ids + ")");
	}

	public VisitRecord getVisitRecord(long id) {
		String sql = "select v.id, v.customer, v.customer_manager, v.type, v.visit_time, v.leave_time, v.content, v.city, v.gps, v.task_id, v.images, v.result_code, v.cost, c.name as cname, c.number as cnumber, c.sell_number as csell_number, c.gps as cgps, m.name as mname, m.username as musername, t.reject from ((visit_record v left join customer c on  v.customer = c.id) left join customer_manager m on v.customer_manager = m.id) left join task t on t.id = v.task_id where 1 = 1 and v.id = "
				+ id;

		List records = this.jdbcTemplate.query(sql, new VisitRecordRowMapper());
		if (records.size() < 1) {
			return null;
		}
		return (VisitRecord) records.get(0);
	}

	public PagingSet<VisitRecord> queryVisitRecords(int start, int limit, String condition,
			String sort, String dir) {
		String sql = "select v.id, v.customer, v.customer_manager, v.type, v.visit_time, v.leave_time, v.content, v.city, v.gps, v.task_id, v.images, v.result_code, v.cost, c.name as cname, c.number as cnumber, c.sell_number as csell_number, c.gps as cgps, m.name as mname, m.username as musername, t.reject from ((visit_record v left join customer c on  v.customer = c.id) left join customer_manager m on v.customer_manager = m.id) left join task t on v.task_id = t.id where 1 = 1 "
				+ condition;
		if (sort != null && !sort.equals("") && dir != null && !dir.equals("")) {
			sql += " order by " + sort + " " + dir;
		}
		PagingHelper h = new PagingHelper(this.jdbcTemplate);
		h.setInParameters(sql, start, limit);
		PagingSet pageSet = h.handle(new VisitRecordRowMapper());
		return pageSet;
	}
	
	public List<VisitRecord> queryVisitRecordsByIds(String ids) {
		String sql = "select v.id, v.customer, v.customer_manager, v.type, v.visit_time, v.leave_time, v.content, v.city, v.gps, v.task_id, v.images, v.result_code, v.cost, c.name as cname, c.number as cnumber, c.sell_number as csell_number, c.gps as cgps, m.name as mname, m.username as musername, t.reject from ((visit_record v left join customer c on  v.customer = c.id) left join customer_manager m on v.customer_manager = m.id) left join task t on t.id = v.task_id where v.id in (" + ids + ")";
		return this.jdbcTemplate.query(sql, new VisitRecordRowMapper());
	}

	public void update(VisitRecord record) {
		this.jdbcTemplate
				.update("update visit_record set cost = ?, city = ?, gps = ?, result_code = ?, type = ?, leave_time = ?, visit_time = ? where id = ?",
						new Object[] { Integer.valueOf(record.getCostTime()), record.getCity(),
								record.getGps(), Integer.valueOf(record.getResultCode()),
								Integer.valueOf(record.getType()), record.getLeaveTime(),
								record.getVisitTime(), Long.valueOf(record.getId()) });
	}

	public VisitRecord getVisitRecordByTaskId(long taskId) {
		String sql = "select v.id, v.customer, v.customer_manager, v.type, v.visit_time, v.leave_time, v.content, v.city, v.gps, v.task_id, v.images, v.result_code, v.cost, c.name as cname, c.number as cnumber, c.sell_number as csell_number, c.gps as cgps, m.name as mname, m.username as musername, t.reject from ((visit_record v left join customer c on  v.customer = c.id) left join customer_manager m on v.customer_manager = m.id) left join task t on t.id = v.task_id where 1 = 1 and v.task_id = "
				+ taskId;

		List records = this.jdbcTemplate.query(sql, new VisitRecordRowMapper());
		if (records.size() < 1) {
			return null;
		}
		return (VisitRecord) records.get(0);
	}

	public List<RecordDetail> getDetails(long id) {
		return this.jdbcTemplate
				.query("select d.id, d.content from record_detail d left join visit_record r on d.record_id = r.id left join task t on t.id = r.task_id where t.id = "
						+ id, new RowMapper<RecordDetail>() {
					public RecordDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
						RecordDetail detail = new RecordDetail();
						detail.setId(rs.getLong("d.id"));
						detail.setContent(rs.getString("d.content"));
						return detail;
					}
				});
	}

	public void submit(String ids, long serviceId) {
		this.jdbcTemplate.update("update record_detail set complete = 1 where id in (" + ids + ")");
	}

	public static class VisitRecordRowMapper implements RowMapper<VisitRecord> {
		public VisitRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			VisitRecord record = new VisitRecord();
			Customer customer = new Customer();
			customer.setId(Long.valueOf(rs.getLong("v.customer")));
			customer.setName(rs.getString("cname"));
			customer.setNumber(rs.getString("cnumber"));
			customer.setSellNumber(rs.getString("csell_number"));
			customer.setGps(rs.getString("cgps"));
			record.setCustomer(customer);
			CustomerManager manager = new CustomerManager();
			manager.setId(Long.valueOf(rs.getLong("v.customer_manager")));
			manager.setName(rs.getString("mname"));
			manager.setUsername(rs.getString("musername"));
			record.setId(rs.getLong("v.id"));
			record.setCustomerManager(manager);
			record.setImages(rs.getInt("v.images"));
			record.setCostTime(rs.getInt("v.cost"));
			record.setResultCode(rs.getInt("v.result_code"));
			record.setTaskId(rs.getLong("v.task_id"));
			record.setCity(rs.getString("v.city"));
			record.setGps(rs.getString("v.gps"));
			record.setType(rs.getInt("v.type"));
			record.setContent(rs.getString("v.content"));
			record.setVisitTime(rs.getTimestamp("v.visit_time"));
			record.setLeaveTime(rs.getTimestamp("v.leave_time"));
			record.setReject(rs.getInt("t.reject"));
			return record;
		}
	}
}