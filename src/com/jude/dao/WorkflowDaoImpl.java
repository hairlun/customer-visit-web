package com.jude.dao;

import com.jude.entity.Customer;
import com.jude.entity.CustomerManager;
import com.jude.entity.Department;
import com.jude.entity.RecordDetail;
import com.jude.entity.Workflow;
import com.jude.entity.WorkflowReply;
import com.jude.util.PagingHelper;
import com.jude.util.PagingSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("workflowDao")
public class WorkflowDaoImpl implements WorkflowDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addWorkflow(Workflow workflow) {
		this.jdbcTemplate
				.update("insert into workflow(id, customer_id, address, receive_time, problem_finder_id, handle_time, handler_id, solved_time, description, remark, response_network) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						new Object[] { workflow.getId(), workflow.getCustomer().getId(),
								workflow.getAddress(), workflow.getReceiveTime(),
								workflow.getProblemFinder().getId(), workflow.getHandleTime(),
								workflow.getHandler().getId(), workflow.getSolvedTime(),
								workflow.getDescription(), workflow.getRemark(),
								workflow.getResponseNetwork() });
	}

	public void deleteWorkflow(String ids) {
		this.jdbcTemplate.update("delete from workflow where id in (" + ids + ")");
	}

	public void updateWorkflow(Workflow workflow) {
		this.jdbcTemplate
				.update("update workflow set customer_id, address, receive_time, problem_finder_id, handle_time, handler_id, solved_time, description, remark, response_network = ? where id = ?",
						new Object[] { workflow.getCustomer().getId(), workflow.getAddress(),
								workflow.getReceiveTime(), workflow.getHandleTime(),
								workflow.getHandler(), workflow.getSolvedTime(),
								workflow.getDescription(), workflow.getRemark(),
								workflow.getResponseNetwork(), workflow.getId() });
	}

	public Workflow getWorkflow(long id) {
		String sql = "select w.id, w.address, w.receive_time, w.handle_time, w.solved_time, w.description, w.remark, w.response_network, c.id as cid, c.name as cname, c.sell_number as csell_number, c.store_name as cstore_name, c.number as cnumber, c.phone_number as cphone_number, c.backup_number as cbackup_number, c.address as caddress, c.gps as cgps, cm.id as cmid, cm.name as cmname, cm.username as cmusername, cmd.id as cmdid, cmd.name as cmdname, pf.id as pfid, pf.name as pfname, pf.username as pfusername, pfd.id as pfdid, pfd.name as pfdname, h.id as hid, h.name as hname, h.username as husername, hd.id as hdid, hd.name as hdname, i.id as iid, i.name as iname from workflow w LEFT JOIN customer c on w.customer_id = c.id LEFT JOIN customer_manager cm on c.manager_id = cm.id LEFT JOIN department_manager_relation cmdr on cmdr.manager_id = cm.id LEFT JOIN department cmd on cmdr.department_id = cmd.id LEFT JOIN customer_manager pf on w.problem_finder_id = pf.id LEFT JOIN department_manager_relation pfdr on pfdr.manager_id = pf.id LEFT JOIN department pfd on pfd.id = pfdr.department_id LEFT JOIN customer_manager h on w.handler_id = h.id LEFT JOIN department_manager_relation hdr on hdr.manager_id = h.id LEFT JOIN department hd on hdr.department_id = hd.id LEFT JOIN workflow_interactive_item_relation wir on w.id = wir.workflow_id LEFT JOIN interactive_item i on wir.item_id = i.id where w.id = "
				+ id;

		List workflows = this.jdbcTemplate.query(sql, new WorkflowRowMapper());
		if (workflows.size() < 1) {
			return null;
		}
		return (Workflow) workflows.get(0);
	}

	public PagingSet<Workflow> queryWorkflows(int start, int limit, String condition, String sort,
			String dir) {
		String sql = "select w.id, w.address, w.receive_time, w.handle_time, w.solved_time, w.description, w.remark, w.response_network, c.id as cid, c.name as cname, c.sell_number as csell_number, c.store_name as cstore_name, c.number as cnumber, c.phone_number as cphone_number, c.backup_number as cbackup_number, c.address as caddress, c.gps as cgps, cm.id as cmid, cm.name as cmname, cm.username as cmusername, cmd.id as cmdid, cmd.name as cmdname, pf.id as pfid, pf.name as pfname, pf.username as pfusername, pfd.id as pfdid, pfd.name as pfdname, h.id as hid, h.name as hname, h.username as husername, hd.id as hdid, hd.name as hdname, i.id as iid, i.name as iname from workflow w LEFT JOIN customer c on w.customer_id = c.id LEFT JOIN customer_manager cm on c.manager_id = cm.id LEFT JOIN department_manager_relation cmdr on cmdr.manager_id = cm.id LEFT JOIN department cmd on cmdr.department_id = cmd.id LEFT JOIN customer_manager pf on w.problem_finder_id = pf.id LEFT JOIN department_manager_relation pfdr on pfdr.manager_id = pf.id LEFT JOIN department pfd on pfd.id = pfdr.department_id LEFT JOIN customer_manager h on w.handler_id = h.id LEFT JOIN department_manager_relation hdr on hdr.manager_id = h.id LEFT JOIN department hd on hdr.department_id = hd.id LEFT JOIN workflow_interactive_item_relation wir on w.id = wir.workflow_id LEFT JOIN interactive_item i on wir.item_id = i.id where 1 = 1 ";
		if (condition != null && !condition.equals("")) {
			sql = sql + condition;
		}
		if (sort != null && !sort.equals("") && dir != null && !dir.equals("")) {
			sql += " order by " + sort + " " + dir;
		}
		PagingHelper h = new PagingHelper(this.jdbcTemplate);
		h.setInParameters(sql, start, limit);
		PagingSet pageSet = h.handle(new WorkflowRowMapper());
		return pageSet;
	}

	public List<Workflow> queryWorkflowsByIds(String ids) {
		String sql = "select w.id, w.address, w.receive_time, w.handle_time, w.solved_time, w.description, w.remark, w.response_network, c.id as cid, c.name as cname, c.sell_number as csell_number, c.store_name as cstore_name, c.number as cnumber, c.phone_number as cphone_number, c.backup_number as cbackup_number, c.address as caddress, c.gps as cgps, cm.id as cmid, cm.name as cmname, cm.username as cmusername, cmd.id as cmdid, cmd.name as cmdname, pf.id as pfid, pf.name as pfname, pf.username as pfusername, pfd.id as pfdid, pfd.name as pfdname, h.id as hid, h.name as hname, h.username as husername, hd.id as hdid, hd.name as hdname, i.id as iid, i.name as iname from workflow w LEFT JOIN customer c on w.customer_id = c.id LEFT JOIN customer_manager cm on c.manager_id = cm.id LEFT JOIN department_manager_relation cmdr on cmdr.manager_id = cm.id LEFT JOIN department cmd on cmdr.department_id = cmd.id LEFT JOIN customer_manager pf on w.problem_finder_id = pf.id LEFT JOIN department_manager_relation pfdr on pfdr.manager_id = pf.id LEFT JOIN department pfd on pfd.id = pfdr.department_id LEFT JOIN customer_manager h on w.handler_id = h.id LEFT JOIN department_manager_relation hdr on hdr.manager_id = h.id LEFT JOIN department hd on hdr.department_id = hd.id LEFT JOIN workflow_interactive_item_relation wir on w.id = wir.workflow_id LEFT JOIN interactive_item i on wir.item_id = i.id where w.id in ("
				+ ids + ")";
		return this.jdbcTemplate.query(sql, new WorkflowRowMapper());
	}

	public static class WorkflowRowMapper implements RowMapper<Workflow> {
		public Workflow mapRow(ResultSet rs, int rowNum) throws SQLException {
			Workflow workflow = new Workflow();
			Customer customer = new Customer();
			customer.setId(Long.valueOf(rs.getLong("cid")));
			customer.setName(rs.getString("cname"));
			customer.setNumber(rs.getString("cnumber"));
			customer.setSellNumber(rs.getString("csell_number"));
			customer.setStoreName(rs.getString("cstore_name"));
			customer.setPhoneNumber(rs.getString("cphone_number"));
			customer.setBackupNumber(rs.getString("cbackup_number"));
			customer.setAddress(rs.getString("caddress"));
			customer.setGps(rs.getString("cgps"));
			CustomerManager customerManager = new CustomerManager();
			customerManager.setId(Long.valueOf(rs.getLong("cmid")));
			customerManager.setName(rs.getString("cmname"));
			customerManager.setUsername(rs.getString("cmusername"));
			Department cmd = new Department();
			cmd.setId(Long.valueOf(rs.getLong("cmdid")));
			cmd.setName(rs.getString("cmdname"));
			customerManager.setDepartment(cmd);
			customer.setCustomerManager(customerManager);
			workflow.setCustomer(customer);
			CustomerManager problemFinder = new CustomerManager();
			problemFinder.setId(Long.valueOf(rs.getLong("pfid")));
			problemFinder.setName(rs.getString("pfname"));
			problemFinder.setUsername(rs.getString("pfusername"));
			Department pfd = new Department();
			pfd.setId(Long.valueOf(rs.getLong("pfdid")));
			pfd.setName(rs.getString("pfdname"));
			problemFinder.setDepartment(pfd);
			workflow.setProblemFinder(problemFinder);
			CustomerManager handler = new CustomerManager();
			handler.setId(Long.valueOf(rs.getLong("hid")));
			handler.setName(rs.getString("hname"));
			handler.setUsername(rs.getString("husername"));
			Department hd = new Department();
			hd.setId(Long.valueOf(rs.getLong("hdid")));
			hd.setName(rs.getString("hdname"));
			handler.setDepartment(hd);
			workflow.setHandler(handler);
			workflow.setId(rs.getLong("w.id"));
			workflow.setAddress(rs.getString("w.address"));
			workflow.setReceiveTime(rs.getTimestamp("w.receive_time"));
			workflow.setHandleTime(rs.getTimestamp("w.handle_time"));
			workflow.setSolvedTime(rs.getTimestamp("w.solved_time"));
			workflow.setDescription(rs.getString("w.description"));
			workflow.setRemark(rs.getString("w.remark"));
			workflow.setResponseNetwork(rs.getString("w.response_network"));
			
			return workflow;
		}
	}

	@Override
	public void addWorkflowReply(WorkflowReply paramWorkflowReply) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteWorkflowReply(String paramString) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WorkflowReply getWorkflowReply(long paramLong) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateWorkflowReply(WorkflowReply paramWorkflowReply) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PagingSet<WorkflowReply> queryWorkflowReplies(int paramInt1, int paramInt2,
			String paramString1, String paramString2, String paramString3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Workflow> queryWorkflowRepliesByIds(String paramString) {
		// TODO Auto-generated method stub
		return null;
	}
}