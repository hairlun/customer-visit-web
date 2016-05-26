package com.jude.dao;

import com.jude.dao.VisitRecordDaoImpl.VisitRecordRowMapper;
import com.jude.entity.Customer;
import com.jude.entity.CustomerManager;
import com.jude.entity.RecordDetail;
import com.jude.entity.Task;
import com.jude.entity.VisitRecord;
import com.jude.util.IdUtil;
import com.jude.util.PagingHelper;
import com.jude.util.PagingSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("taskDao")
public class TaskDaoImpl implements TaskDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private VisitRecordDao recordDao;
	private static SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");

	private static SimpleDateFormat ssf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public void addTasks(List<Task> tasks, List<RecordDetail> cs) {
		int idx = 0;
		for (Task task : tasks) {
			addTask(task);
			idx++;
			VisitRecord record = new VisitRecord();
			record.setId(IdUtil.getId() + idx);
			record.setTaskId(task.getId());
			record.setCustomer(task.getCustomer());
			record.setCustomerManager(task.getManager());
			record.setContent(task.getContent());
			record.setType(0);
			this.recordDao.addVisitRecord(record, cs);
		}
	}

	public void delete(String taskId) {
		this.jdbcTemplate.update("delete from task where id = ?", new Object[] { taskId });
	}

	public List<Task> getManagerTask(String username) {
		String sql = "select t.complete, t.reply, t.content, t.start_time, t.end_time, t.create_time, t.id, c.id, c.name, c.sell_number, c.phone_number, c.backup_number, m.id from task t left join customer c on t.customer = c.id left join customer_manager m on t.manager = m.id where m.username = ?";
		return this.jdbcTemplate.query(sql, new Object[] { username }, new TaskRowMapper());
	}

	public Task getTask(String taskId) {
		List list = this.jdbcTemplate
				.query("select t.complete, t.reply, t.content, t.start_time, t.end_time, t.create_time, t.id, c.id, c.name, c.sell_number, c.phone_number, c.backup_number, m.id from task t left join customer c on t.customer = c.id left join customer_manager m on t.manager = m.id where t.id = '"
						+ taskId + "'", new TaskRowMapper());
		if ((list == null) || (list.size() == 0)) {
			return null;
		}
		return (Task) list.get(0);
	}

	public void addTask(Task task) {
		StringBuffer sb = new StringBuffer(
				"insert into task(id, start_time, end_time, manager, customer, content, create_time) values");
		sb.append("('").append(task.getId()).append("',").append(sf.format(task.getStart()))
				.append(",").append(sf.format(task.getEnd())).append(",")
				.append(task.getManager().getId()).append(",").append(task.getCustomer().getId())
				.append(",'").append(task.getContent()).append("','")
				.append(ssf.format(new Date())).append("'),");

		this.jdbcTemplate.update(sb.substring(0, sb.length() - 1));
	}

	public List<Task> getManagerTask(long managerId) {
		String sql = "select t.complete, t.reply, t.content, t.start_time, t.end_time, t.create_time, t.id, c.id, c.name, c.sell_number, c.phone_number, c.backup_number, m.id from task t left join customer c on t.customer = c.id left join customer_manager m on t.manager = m.id where m.id = ?";
		return this.jdbcTemplate.query(sql, new Object[] { Long.valueOf(managerId) },
				new TaskRowMapper());
	}

	public PagingSet<Task> getManagerTask(long managerId, int start, int limit, int type, String keyword,
			String startTime, String endTime) {
		StringBuffer sb = new StringBuffer();
		sb.append("select t.complete, t.reply, t.content, t.start_time, t.end_time, t.create_time, t.id, c.id, c.name, c.sell_number, c.phone_number, c.backup_number, m.id from task t left join customer c on t.customer = c.id left join customer_manager m on t.manager = m.id where m.id = ").append(managerId);
		sb.append(" and (t.complete = ").append(type).append(" or t.reject = 1)");
		if (!StringUtils.isBlank(keyword)) {
			sb.append(" and (c.name like '%").append(keyword).append("%'");
			sb.append(" or c.sell_number like '%").append(keyword).append("%'");
			sb.append(" or c.phone_number like '%").append(keyword).append("%'");
			sb.append(" or c.backup_number like '%").append(keyword).append("%')");
		}
		PagingHelper h = new PagingHelper(this.jdbcTemplate);
		h.setInParameters(sb.toString(), start, limit);
		PagingSet<Task> pageSet = h.handle(new TaskRowMapper());
		return pageSet;
	}
	
	public void reject(String taskIds, String reason) {
		this.jdbcTemplate.update(
				"update task set complete = 0, reject = 1, reply = ? where id in (" + taskIds + ")",
				new Object[] { reason });
	}
	
	public void complete(String taskId) {
		this.jdbcTemplate.update(
				"update task set complete = 1, reject = 0 where id = ?",
				new Object[] { taskId });
	}

	public static class TaskRowMapper implements RowMapper<Task> {
		public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
			Task task = new Task();
			task.setId(rs.getString("t.id"));
			task.setContent(rs.getString("t.content"));
			task.setStart(rs.getDate("t.start_time"));
			task.setEnd(rs.getDate("t.end_time"));
			task.setCreate(rs.getTimestamp("t.create_time"));
			task.setComplete(rs.getInt("t.complete"));
			task.setReply(rs.getString("t.reply"));
			Customer customer = new Customer();
			customer.setId(Long.valueOf(rs.getLong("c.id")));
			customer.setName(rs.getString("c.name"));
			customer.setSellNumber(rs.getString("c.sell_number"));
			customer.setPhoneNumber(rs.getString("c.phone_number"));
			customer.setBackupNumber(rs.getString("c.backup_number"));
			task.setCustomer(customer);
			CustomerManager manager = new CustomerManager();
			manager.setId(Long.valueOf(rs.getLong("m.id")));
			task.setManager(manager);
			return task;
		}
	}
}