package com.jude.service;

import com.jude.entity.RecordDetail;
import com.jude.entity.VisitRecord;
import com.jude.util.PagingSet;
import java.util.List;

public abstract interface VisitRecordService {
	public abstract void addVisitRecord(VisitRecord paramVisitRecord, List<RecordDetail> paramList);

	public abstract void deleteVisitRecord(String paramString1, String paramString2);

	public abstract VisitRecord getVisitRecord(String paramString);

	public abstract PagingSet<VisitRecord> queryVisitRecords(int paramInt1, int paramInt2,
			String paramString1, String paramString2, String paramString3);

	public abstract void update(VisitRecord paramVisitRecord);

	public abstract VisitRecord getVisitRecordByTaskId(String paramString);
	
	public abstract List<VisitRecord> queryVisitRecordsByIds(String paramString);

	public abstract List<RecordDetail> getDetails(String paramString);

	public abstract void submit(String paramString1, String paramString2);
}