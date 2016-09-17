package com.jude.dao;

import com.jude.entity.WorkflowReply;
import com.jude.entity.Workflow;
import com.jude.util.PagingSet;
import java.util.List;

public abstract interface WorkflowDao {
	public abstract void addWorkflow(Workflow paramWorkflow);

	public abstract void deleteWorkflow(String paramString);

	public abstract void updateWorkflow(Workflow paramWorkflow);

	public abstract Workflow getWorkflow(long paramLong);

	public abstract PagingSet<Workflow> queryWorkflows(int paramInt1, int paramInt2,
			String paramString1, String paramString2, String paramString3);
	
	public abstract List<Workflow> queryWorkflowsByIds(String paramString);
	
	public abstract void addWorkflowReply(WorkflowReply paramWorkflowReply);
	
	public abstract void deleteWorkflowReply(String paramString);
	
	public abstract WorkflowReply getWorkflowReply(long paramLong);
	
	public abstract void updateWorkflowReply(WorkflowReply paramWorkflowReply);

	public abstract PagingSet<WorkflowReply> queryWorkflowReplies(int paramInt1, int paramInt2,
			String paramString1, String paramString2, String paramString3);
	
	public abstract List<Workflow> queryWorkflowRepliesByIds(String paramString);
}