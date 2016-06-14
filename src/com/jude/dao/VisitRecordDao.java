package com.jude.dao;

import com.jude.entity.RecordDetail;
import com.jude.entity.VisitRecord;
import com.jude.util.PagingSet;
import java.util.List;

public abstract interface VisitRecordDao {
	public abstract void addVisitRecord(VisitRecord paramVisitRecord, List<RecordDetail> paramList);

	public abstract void deleteVisitRecord(String paramString);

	public abstract VisitRecord getVisitRecord(long paramLong);

	public abstract PagingSet<VisitRecord> queryVisitRecords(int paramInt1, int paramInt2,
			String paramString1, String paramString2, String paramString3);
	
	public abstract List<VisitRecord> queryVisitRecordsByIds(String paramString);

	public abstract void update(VisitRecord paramVisitRecord);

	public abstract VisitRecord getVisitRecordByTaskId(long paramLong);

	public abstract List<RecordDetail> getDetails(long paramLong);

	public abstract void submit(String paramString1, long paramLong1);
}