package com.jude.dao;

import com.jude.entity.Department;
import com.jude.util.PagingSet;

public abstract interface DepartmentDao {
	public abstract void addDepartment(Department paramDepartment);

	public abstract void deleteDepartments(String paramString);

	public abstract Department getDepartment(String paramString);

	public abstract boolean nameExist(String paramString);

	public abstract void updateDepartment(Department paramDepartment);

	public abstract Department getDepartment(long paramLong);

	public abstract PagingSet<Department> getDepartments(int paramInt1, int paramInt2,
			String paramString3, String paramString4);

	public abstract void joinDepartment(long paramLong1, long paramLong2);

	public abstract void exitDepartment(long paramLong1, long paramLong2);
}