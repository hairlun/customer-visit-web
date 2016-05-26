package com.jude.dao;

import com.jude.entity.ModelSwitch;

public abstract interface SwitchDao {
	public abstract void update(ModelSwitch paramModelSwitch);

	public abstract ModelSwitch query();
}