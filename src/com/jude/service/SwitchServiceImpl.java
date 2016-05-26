package com.jude.service;

import com.jude.dao.SwitchDao;
import com.jude.entity.ModelSwitch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("switchService")
public class SwitchServiceImpl implements SwitchService {

	@Autowired
	private SwitchDao switchDao;

	public void update(ModelSwitch modelSwitch) {
		this.switchDao.update(modelSwitch);
	}

	public ModelSwitch query() {
		return this.switchDao.query();
	}
}