package com.jude.service;

import com.jude.dao.DepartmentDao;
import com.jude.entity.Department;
import com.jude.util.PagingSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentDao departmentDao;

	public void addDepartment(Department department) {
		this.departmentDao.addDepartment(department);
	}

	public void deleteDepartments(String ids) {
		this.departmentDao.deleteDepartments(ids);
	}

	public Department getDepartment(String username) {
		return this.departmentDao.getDepartment(username);
	}

	public PagingSet<Department> getDepartments(int start, int pageSize,
			String sort, String dir) {
		return this.departmentDao.getDepartments(start, pageSize, sort, dir);
	}

	public boolean nameExist(String username) {
		return this.departmentDao.nameExist(username);
	}

	public void updateDepartment(Department manager) {
		this.departmentDao.updateDepartment(manager);
	}

	public Department getDepartment(long id) {
		return this.departmentDao.getDepartment(id);
	}

	public void joinDepartment(long managerId, long deartmentId) {
		this.departmentDao.joinDepartment(managerId, deartmentId);
	}

	public void exitDepartment(long managerId, long deartmentId) {
		this.departmentDao.exitDepartment(managerId, deartmentId);
	}
}