package com.jude.util;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

public abstract interface PagingStoredProcedure {
	public abstract void setInParameters(String paramString, int paramInt1, int paramInt2);

	public abstract void setInParameters(String paramString, int paramInt1, int paramInt2,
			int paramInt3);

	public abstract <T> PagingSet<T> handle(ResultSetExtractor<T> paramResultSetExtractor);

	public abstract <T> PagingSet<T> handle(RowCallbackHandler paramRowCallbackHandler);

	public abstract <T> PagingSet<T> handle(RowMapper<T> paramRowMapper);
}