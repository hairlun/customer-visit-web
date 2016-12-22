package com.jude.dao;

import com.jude.entity.Customer;
import com.jude.entity.CustomerManager;
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
                .update("insert into workflow(id, customer_id, address, receive_time, problem_finder_id, handle_time, handler_id, solved_time, description, remark) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        new Object[] { workflow.getId(),
                                workflow.getCustomer().getId(),
                                workflow.getAddress(),
                                workflow.getReceiveTime(),
                                workflow.getProblemFinder().getId(),
                                workflow.getHandleTime(),
                                workflow.getHandler().getId(),
                                workflow.getSolvedTime(),
                                workflow.getDescription(), workflow.getRemark() });
    }

    public void deleteWorkflows(String ids) {
        this.jdbcTemplate.update("delete from workflow where id in (" + ids
                + ")");
    }

    public void updateWorkflow(Workflow workflow) {
        this.jdbcTemplate
                .update("update workflow set customer_id = ?, address = ?, receive_time = ?, problem_finder_id = ?, handle_time = ?, handler_id = ?, solved_time = ?, description = ?, remark = ? where id = ?",
                        new Object[] { workflow.getCustomer().getId(),
                                workflow.getAddress(),
                                workflow.getReceiveTime(),
                                workflow.getProblemFinder().getId(),
                                workflow.getHandleTime(),
                                workflow.getHandler().getId(),
                                workflow.getSolvedTime(),
                                workflow.getDescription(),
                                workflow.getRemark(), workflow.getId() });
    }

    public Workflow getWorkflow(long id) {
        String sql = "select w.id, w.address, w.receive_time, w.handle_time, w.solved_time, w.description, w.remark, c.id as cid, c.name as cname, c.sell_number as csell_number, c.store_name as cstore_name, c.number as cnumber, c.phone_number as cphone_number, c.backup_number as cbackup_number, c.address as caddress, c.gps as cgps, cm.id as cmid, cm.name as cmname, cm.username as cmusername, cm.department as cmdepartment, cm.area as cmarea, pf.id as pfid, pf.name as pfname, pf.username as pfusername, pf.department as pfdepartment, pf.area as pfarea, h.id as hid, h.name as hname, h.username as husername, h.department as hdepartment, h.area as harea, i.id as iid, i.name as iname from workflow w LEFT JOIN customer c on w.customer_id = c.id LEFT JOIN customer_manager cm on c.manager_id = cm.id LEFT JOIN customer_manager pf on w.problem_finder_id = pf.id LEFT JOIN customer_manager h on w.handler_id = h.id LEFT JOIN workflow_interactive_item_relation wir on w.id = wir.workflow_id LEFT JOIN interactive_item i on wir.item_id = i.id where w.id = "
                + id;

        List workflows = this.jdbcTemplate.query(sql, new WorkflowRowMapper());
        if (workflows.size() < 1) {
            return null;
        }
        return (Workflow) workflows.get(0);
    }

    public PagingSet<Workflow> queryWorkflows(int start, int limit,
            String condition, String sort, String dir) {
        String sql = "select w.id, w.address, w.receive_time, w.handle_time, w.solved_time, w.description, w.remark, c.id as cid, c.name as cname, c.sell_number as csell_number, c.store_name as cstore_name, c.number as cnumber, c.phone_number as cphone_number, c.backup_number as cbackup_number, c.address as caddress, c.gps as cgps, cm.id as cmid, cm.name as cmname, cm.username as cmusername, cm.department as cmdepartment, cm.area as cmarea, pf.id as pfid, pf.name as pfname, pf.username as pfusername, pf.department as pfdepartment, pf.area as pfarea, h.id as hid, h.name as hname, h.username as husername, h.department as hdepartment, h.area as harea, i.id as iid, i.name as iname from workflow w LEFT JOIN customer c on w.customer_id = c.id LEFT JOIN customer_manager cm on c.manager_id = cm.id LEFT JOIN customer_manager pf on w.problem_finder_id = pf.id LEFT JOIN customer_manager h on w.handler_id = h.id LEFT JOIN workflow_interactive_item_relation wir on w.id = wir.workflow_id LEFT JOIN interactive_item i on wir.item_id = i.id where 1 = 1 ";
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
        String sql = "select select w.id, w.address, w.receive_time, w.handle_time, w.solved_time, w.description, w.remark, c.id as cid, c.name as cname, c.sell_number as csell_number, c.store_name as cstore_name, c.number as cnumber, c.phone_number as cphone_number, c.backup_number as cbackup_number, c.address as caddress, c.gps as cgps, cm.id as cmid, cm.name as cmname, cm.username as cmusername, cm.department as cmdepartment, cm.area as cmarea, pf.id as pfid, pf.name as pfname, pf.username as pfusername, pf.department as pfdepartment, pf.area as pfarea, h.id as hid, h.name as hname, h.username as husername, h.department as hdepartment, h.area as harea, i.id as iid, i.name as iname from workflow w LEFT JOIN customer c on w.customer_id = c.id LEFT JOIN customer_manager cm on c.manager_id = cm.id LEFT JOIN customer_manager pf on w.problem_finder_id = pf.id LEFT JOIN customer_manager h on w.handler_id = h.id LEFT JOIN workflow_interactive_item_relation wir on w.id = wir.workflow_id LEFT JOIN interactive_item i on wir.item_id = i.id where w.id in ("
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
            customerManager.setDepartment(rs.getString("cmdepartment"));
            customerManager.setArea(rs.getString("cmarea"));
            customer.setCustomerManager(customerManager);
            workflow.setCustomer(customer);
            CustomerManager problemFinder = new CustomerManager();
            problemFinder.setId(Long.valueOf(rs.getLong("pfid")));
            problemFinder.setName(rs.getString("pfname"));
            problemFinder.setUsername(rs.getString("pfusername"));
            problemFinder.setDepartment(rs.getString("pfdepartment"));
            problemFinder.setArea(rs.getString("pfarea"));
            workflow.setProblemFinder(problemFinder);
            CustomerManager handler = new CustomerManager();
            handler.setId(Long.valueOf(rs.getLong("hid")));
            handler.setName(rs.getString("hname"));
            handler.setUsername(rs.getString("husername"));
            handler.setDepartment(rs.getString("hdepartment"));
            handler.setArea(rs.getString("harea"));
            workflow.setHandler(handler);
            workflow.setId(rs.getLong("w.id"));
            workflow.setAddress(rs.getString("w.address"));
            workflow.setReceiveTime(rs.getTimestamp("w.receive_time"));
            workflow.setHandleTime(rs.getTimestamp("w.handle_time"));
            workflow.setSolvedTime(rs.getTimestamp("w.solved_time"));
            workflow.setDescription(rs.getString("w.description"));
            workflow.setRemark(rs.getString("w.remark"));

            return workflow;
        }
    }

    @Override
    public void addWorkflowReply(WorkflowReply workflowReply) {
        this.jdbcTemplate
                .update("insert into workflow_reply(id, reply, remark, workflow_id, order_num, handle_time, handler_id, current_handler_id) values(?, ?, ?, ?, ?, ?, ?, ?)",
                        new Object[] { workflowReply.getId(),
                                workflowReply.getReply(),
                                workflowReply.getRemark(),
                                workflowReply.getWorkflowId(),
                                workflowReply.getOrderNum(),
                                workflowReply.getHandleTime(),
                                workflowReply.getHandler().getId(),
                                workflowReply.getCurrentHandler().getId() });
    }

    @Override
    public void deleteWorkflowReplys(String ids) {
        this.jdbcTemplate.update("delete from workflow_reply where id in ("
                + ids + ")");
    }

    @Override
    public WorkflowReply getWorkflowReply(long id) {
        String sql = "select w.id, w.reply, w.remark, w.workflow_id, w.order_num, w.handle_time, w.handler_id, h.id as hid, h.name as hname, h.username as husername, h.department as hdepartment, h.area as harea, ch.id as chid, ch.name as chname, ch.username as chusername, ch.department as chdepartment, ch.area as charea from workflow_reply w LEFT JOIN customer_manager h on w.handler_id = h.id LEFT JOIN customer_manager ch on w.current_handler_id = ch.id where w.id = "
                + id;

        List<WorkflowReply> workflowReplies = this.jdbcTemplate.query(sql,
                new WorkflowReplyRowMapper());
        if (workflowReplies.size() < 1) {
            return null;
        }
        return (WorkflowReply) workflowReplies.get(0);
    }

    @Override
    public void updateWorkflowReply(WorkflowReply workflowReply) {
        this.jdbcTemplate
                .update("update workflow_reply set reply = ?, remark = ?, workflow_id = ?, order_num = ?, handle_time = ?, handler_id = ?, current_handler_id = ? where id = ?",
                        new Object[] { workflowReply.getReply(),
                                workflowReply.getRemark(),
                                workflowReply.getWorkflowId(),
                                workflowReply.getOrderNum(),
                                workflowReply.getHandleTime(),
                                workflowReply.getHandler().getId(),
                                workflowReply.getCurrentHandler().getId(),
                                workflowReply.getId() });
    }

    @Override
    public PagingSet<WorkflowReply> queryWorkflowReplies(int start, int limit,
            String condition, String sort, String dir) {
        String sql = "select w.id, w.reply, w.remark, w.workflow_id, w.order_num, w.handle_time, w.handler_id, h.id as hid, h.name as hname, h.username as husername, h.department as hdepartment, h.area as harea, ch.id as chid, ch.name as chname, ch.username as chusername, ch.department as chdepartment, ch.area as charea from workflow_reply w LEFT JOIN customer_manager h on w.handler_id = h.id LEFT JOIN customer_manager ch on w.current_handler_id = ch.id where 1 = 1 ";
        if (condition != null && !condition.equals("")) {
            sql = sql + condition;
        }
        if (sort != null && !sort.equals("") && dir != null && !dir.equals("")) {
            sql += " order by " + sort + " " + dir;
        }
        PagingHelper h = new PagingHelper(this.jdbcTemplate);
        h.setInParameters(sql, start, limit);
        PagingSet pageSet = h.handle(new WorkflowReplyRowMapper());
        return pageSet;
    }

    @Override
    public List<WorkflowReply> queryWorkflowRepliesByIds(String ids) {
        String sql = "select w.id, w.reply, w.remark, w.workflow_id, w.order_num, w.handle_time, w.handler_id, h.id as hid, h.name as hname, h.username as husername, h.department as hdepartment, h.area as harea, ch.id as chid, ch.name as chname, ch.username as chusername, ch.department as chdepartment, ch.area as charea from workflow_reply w LEFT JOIN customer_manager h on w.handler_id = h.id LEFT JOIN customer_manager ch on w.current_handler_id = ch.id where w.id in ("
                + ids + ")";
        return this.jdbcTemplate.query(sql, new WorkflowReplyRowMapper());
    }

    public static class WorkflowReplyRowMapper implements
            RowMapper<WorkflowReply> {
        public WorkflowReply mapRow(ResultSet rs, int rowNum)
                throws SQLException {
            WorkflowReply workflowReply = new WorkflowReply();
            workflowReply.setId(rs.getLong("w.id"));
            workflowReply.setReply(rs.getString("w.reply"));
            workflowReply.setRemark(rs.getString("w.remark"));
            workflowReply.setWorkflowId(rs.getLong("w.workflow_id"));
            workflowReply.setOrderNum(rs.getInt("w.order_num"));
            workflowReply.setHandleTime(rs.getTimestamp("w.handle_time"));
            CustomerManager handler = new CustomerManager();
            handler.setId(rs.getLong("h.id"));
            handler.setName(rs.getString("h.name"));
            handler.setUsername(rs.getString("h.username"));
            handler.setDepartment(rs.getString("h.department"));
            handler.setArea(rs.getString("h.area"));
            workflowReply.setHandler(handler);
            CustomerManager currentHandler = new CustomerManager();
            currentHandler.setId(rs.getLong("ch.id"));
            currentHandler.setName(rs.getString("ch.name"));
            currentHandler.setUsername(rs.getString("ch.username"));
            currentHandler.setDepartment(rs.getString("ch.department"));
            currentHandler.setArea(rs.getString("ch.area"));
            workflowReply.setCurrentHandler(currentHandler);
            return workflowReply;
        }
    }
}