package com.jude.service;

import com.jude.entity.ModelSwitch;

public abstract interface SwitchService {
	public abstract void update(ModelSwitch paramModelSwitch);

	public abstract ModelSwitch query();
}