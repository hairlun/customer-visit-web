package com.jude.service;

import com.jude.dao.WorkflowDao;
import com.jude.entity.RecordDetail;
import com.jude.entity.Workflow;
import com.jude.entity.WorkflowReply;
import com.jude.util.PagingSet;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("workflowService")
public class WorkflowServiceImpl implements WorkflowService {

	@Autowired
	private WorkflowDao workflowDao;

	public void addWorkflow(Workflow workflow) {
		this.workflowDao.addWorkflow(workflow);
	}

	public void deleteWorkflows(String ids) {
		this.workflowDao.deleteWorkflows(ids);
	}

    public void updateWorkflow(Workflow workflow) {
        this.workflowDao.updateWorkflow(workflow);
    }

	public Workflow getWorkflow(long id) {
		return this.workflowDao.getWorkflow(id);
	}

	public PagingSet<Workflow> queryWorkflows(int start, int limit, String condition,
			String sort, String dir) {
		return this.workflowDao.queryWorkflows(start, limit, condition, sort, dir);
	}
	
	public List<Workflow> queryWorkflowsByIds(String ids) {
		return this.workflowDao.queryWorkflowsByIds(ids);
	}
    
    public void addWorkflowReply(WorkflowReply workflowReply) {
        this.workflowDao.addWorkflowReply(workflowReply);
    }
    
    public void deleteWorkflowReplys(String ids) {
        this.workflowDao.deleteWorkflowReplys(ids);
    }
    
    public WorkflowReply getWorkflowReply(long id) {
        return this.workflowDao.getWorkflowReply(id);
    }
    
    public void updateWorkflowReply(WorkflowReply workflowReply) {
        this.workflowDao.updateWorkflowReply(workflowReply);
    }

    public PagingSet<WorkflowReply> queryWorkflowReplies(int start, int limit,
            String condition, String sort, String dir) {
        return this.workflowDao.queryWorkflowReplies(start, limit, condition, sort, dir);
    }
    
    public List<WorkflowReply> queryWorkflowRepliesByIds(String ids) {
        return this.workflowDao.queryWorkflowRepliesByIds(ids);
    }
}