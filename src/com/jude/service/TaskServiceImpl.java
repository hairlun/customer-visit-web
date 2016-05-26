package com.jude.service;

import com.jude.dao.TaskDao;
import com.jude.entity.RecordDetail;
import com.jude.entity.Task;
import com.jude.util.PagingSet;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("taskService")
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskDao taskDao;

	public void addTask(List<Task> tasks, List<RecordDetail> cs) {
		this.taskDao.addTasks(tasks, cs);
	}

	public void addTask(Task task) {
		this.taskDao.addTask(task);
	}

	public List<Task> getManagerTask(String username) {
		return this.taskDao.getManagerTask(username);
	}

	public Task getTask(String id) {
		return this.taskDao.getTask(id);
	}

	public List<Task> getManagerTask(long managerId) {
		return this.taskDao.getManagerTask(managerId);
	}

	public PagingSet<Task> getManagerTask(long managerId, int start, int limit, int type, String keyword,
			String startTime, String endTime) {
		return this.taskDao.getManagerTask(managerId, start, limit, type, keyword, startTime, endTime);
	}

	public void delete(String taskId) {
		this.taskDao.delete(taskId);
	}
	
	public void complete(String taskId) {
		this.taskDao.complete(taskId);
	}
	
	public void reject(String taskIds, String reason) {
		this.taskDao.reject(taskIds, reason);
	}
}