package com.jude.service;

import com.jude.dao.InteractiveItemDao;
import com.jude.entity.InteractiveItem;
import com.jude.util.PagingSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("interactiveItemService")
public class InteractiveItemServiceImpl implements InteractiveItemService {

	@Autowired
	private InteractiveItemDao interactiveItemDao;

	public void addInteractiveItem(InteractiveItem interactiveItem) {
		this.interactiveItemDao.addInteractiveItem(interactiveItem);
	}

	public void deleteInteractiveItems(String ids) {
		this.interactiveItemDao.deleteInteractiveItems(ids);
	}

	public InteractiveItem getInteractiveItem(String name) {
		return this.interactiveItemDao.getInteractiveItem(name);
	}

	public PagingSet<InteractiveItem> getInteractiveItems(int start, int pageSize,
			String sort, String dir) {
		return this.interactiveItemDao.getInteractiveItems(start, pageSize, sort, dir);
	}

	public boolean nameExist(String name) {
		return this.interactiveItemDao.nameExist(name);
	}

	public void updateInteractiveItem(InteractiveItem interactiveItem) {
		this.interactiveItemDao.updateInteractiveItem(interactiveItem);
	}

	public InteractiveItem getInteractiveItem(long id) {
		return this.interactiveItemDao.getInteractiveItem(id);
	}
}