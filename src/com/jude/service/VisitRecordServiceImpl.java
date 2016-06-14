package com.jude.service;

import com.jude.dao.VisitRecordDao;
import com.jude.entity.RecordDetail;
import com.jude.entity.VisitRecord;
import com.jude.util.PagingSet;
import java.io.File;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("visitRecordService")
public class VisitRecordServiceImpl implements VisitRecordService {

	@Autowired
	private VisitRecordDao visitRecordDao;

	public void addVisitRecord(VisitRecord visitRecord, List<RecordDetail> details) {
		this.visitRecordDao.addVisitRecord(visitRecord, details);
	}

	public void deleteVisitRecord(String ids, String path) {
		this.visitRecordDao.deleteVisitRecord(ids);
		String[] is = ids.split(",");
		for (String id : is)
			for (int i = 0; i < 6; ++i) {
				File file = new File(path + File.separator + id + "_" + i + ".jpg");
				if (file.exists())
					file.delete();
			}
	}

	public VisitRecord getVisitRecord(long id) {
		return this.visitRecordDao.getVisitRecord(id);
	}

	public PagingSet<VisitRecord> queryVisitRecords(int start, int limit, String condition,
			String sort, String dir) {
		return this.visitRecordDao.queryVisitRecords(start, limit, condition, sort, dir);
	}
	
	public List<VisitRecord> queryVisitRecordsByIds(String ids) {
		return this.visitRecordDao.queryVisitRecordsByIds(ids);
	}

	public void update(VisitRecord record) {
		this.visitRecordDao.update(record);
	}

	public VisitRecord getVisitRecordByTaskId(long taskId) {
		return this.visitRecordDao.getVisitRecordByTaskId(taskId);
	}

	public List<RecordDetail> getDetails(long id) {
		return this.visitRecordDao.getDetails(id);
	}

	public void submit(String ids, long serviceId) {
		this.visitRecordDao.submit(ids, serviceId);
	}
}