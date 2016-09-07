package com.jude.service;

import com.jude.entity.InteractiveItem;
import com.jude.util.PagingSet;

public abstract interface InteractiveItemService {
	public abstract void addInteractiveItem(InteractiveItem paramInteractiveItem);

	public abstract void deleteInteractiveItems(String paramString);

	public abstract InteractiveItem getInteractiveItem(String paramString);

	public abstract boolean nameExist(String paramString);

	public abstract void updateInteractiveItem(InteractiveItem paramInteractiveItem);

	public abstract InteractiveItem getInteractiveItem(long paramLong);

	public abstract PagingSet<InteractiveItem> getInteractiveItems(int paramInt1, int paramInt2,
			String paramString3, String paramString4);
}