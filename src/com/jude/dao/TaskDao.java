package com.jude.dao;

import com.jude.entity.RecordDetail;
import com.jude.entity.Task;
import com.jude.util.PagingSet;

import java.util.List;

public abstract interface TaskDao {
	public abstract void addTask(Task paramTask);

	public abstract void addTasks(List<Task> paramList, List<RecordDetail> paramList1);

	public abstract List<Task> getManagerTask(String paramString);

	public abstract Task getTask(long paramLong);

	public abstract List<Task> getManagerTask(long paramLong);

	public abstract PagingSet<Task> getManagerTask(long paramLong1, int paramInt2, int paramInt3,
			int paramInt4, String paramString5, String paramString6, String paramString7);

	public abstract List<Task> getTasksByVisitRecordIds(String paramString);

	public abstract void delete(long paramLong);
	
	public abstract void complete(long paramLong);
	
	public abstract void reject(String paramString1, String paramString2);
}