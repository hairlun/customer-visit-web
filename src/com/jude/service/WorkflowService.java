package com.jude.service;

import com.jude.entity.Workflow;
import com.jude.entity.WorkflowReply;
import com.jude.util.PagingSet;

import java.util.List;

public abstract interface WorkflowService {
	public abstract void addWorkflow(Workflow paramWorkflow);

	public abstract void deleteWorkflows(String paramString1);

    public abstract void updateWorkflow(Workflow paramWorkflow);

	public abstract Workflow getWorkflow(long paramLong);

	public abstract PagingSet<Workflow> queryWorkflows(int paramInt1, int paramInt2,
			String paramString1, String paramString2, String paramString3);
    
    public abstract List<Workflow> queryWorkflowsByIds(String paramString);
    
    public abstract void addWorkflowReply(WorkflowReply paramWorkflowReply);
    
    public abstract void deleteWorkflowReplys(String paramString);
    
    public abstract WorkflowReply getWorkflowReply(long paramLong);
    
    public abstract void updateWorkflowReply(WorkflowReply paramWorkflowReply);

    public abstract PagingSet<WorkflowReply> queryWorkflowReplies(int paramInt1, int paramInt2,
            String paramString1, String paramString2, String paramString3);
    
    public abstract List<WorkflowReply> queryWorkflowRepliesByIds(String paramString);
}