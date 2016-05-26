package com.jude.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

public class PagingHelper {
	private PagingStoredProcedure procedure;

	public PagingHelper(JdbcTemplate jt) {
		if (DbContext.isMySQL())
			this.procedure = new DefaultPagingStoredProcedureImpl(jt, "sp_page");
		else
			throw new IllegalStateException("当前数据库版本分页存储过程不存在！");
	}

	public void setInParameters(String sql, int start, int pageSize) {
		this.procedure.setInParameters(sql, start, pageSize);
	}

	public void setInParameters(String sql, int start, int pageSize, int addRowNumber) {
		this.procedure.setInParameters(sql, start, pageSize, addRowNumber);
	}

	public <T> PagingSet<T> handle(ResultSetExtractor<T> extractor) {
		return this.procedure.handle(extractor);
	}

	public <T> PagingSet<T> handle(RowCallbackHandler handler) {
		return this.procedure.handle(handler);
	}

	public <T> PagingSet<T> handle(RowMapper<T> rowMapper) {
		return this.procedure.handle(rowMapper);
	}
}