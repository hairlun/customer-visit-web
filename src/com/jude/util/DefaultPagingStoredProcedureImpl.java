package com.jude.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.object.StoredProcedure;

class DefaultPagingStoredProcedureImpl extends StoredProcedure implements PagingStoredProcedure {
	private static final Log log = LogFactory.getLog(PagingStoredProcedure.class);
	protected static final String SQL = "sql";
	protected static final String START = "startIdx";
	protected static final String PAGESIZE = "pageSize";
	protected static final String ADDROWNUMBER = "addRowNo";
	protected static final String TOTAL = "totalSize";
	protected static final String RESULTSET = "resultset";
	protected Map<String, Object> inParameterMapper = new LinkedHashMap();

	public DefaultPagingStoredProcedureImpl(JdbcTemplate jt, String name) {
		super(jt, name);
		declareParameters();
	}

	protected void declareParameters() {
		declareParameter(new SqlParameter("sql", 12));
		declareParameter(new SqlParameter("startIdx", 4));
		declareParameter(new SqlParameter("pageSize", 4));
		declareParameter(new SqlParameter("addRowNo", 4));
		declareParameter(new SqlOutParameter("totalSize", 4));
	}

	public void setInParameters(String sql, int start, int pageSize) {
		this.inParameterMapper.put("sql", sql);
		this.inParameterMapper.put("startIdx", Integer.valueOf(start));
		this.inParameterMapper.put("pageSize", Integer.valueOf(pageSize));
		this.inParameterMapper.put("addRowNo", Integer.valueOf(0));
	}

	public void setInParameters(String sql, int start, int pageSize, int addRowNumber) {
		this.inParameterMapper.put("sql", sql);
		this.inParameterMapper.put("startIdx", Integer.valueOf(start));
		this.inParameterMapper.put("pageSize", Integer.valueOf(pageSize));
		this.inParameterMapper.put("addRowNo", Integer.valueOf(addRowNumber));
	}

	protected ParameterMapper getInParameterMapper() {
		return new ParameterMapper() {
			public Map<String, Object> createMap(Connection con) throws SQLException {
				return DefaultPagingStoredProcedureImpl.this.inParameterMapper;
			}
		};
	}

	public <T> PagingSet<T> handle(ResultSetExtractor<T> extractor) {
		declareParameter(new SqlReturnResultSet("resultset", extractor));
		return handle();
	}

	public <T> PagingSet<T> handle(RowCallbackHandler handler) {
		declareParameter(new SqlReturnResultSet("resultset", handler));
		return handle();
	}

	public <T> PagingSet<T> handle(RowMapper<T> rowMapper) {
		declareParameter(new SqlReturnResultSet("resultset", rowMapper));
		return handle();
	}

	protected <T> PagingSet<T> handle() {
		log.debug("Execute page procedure '" + getSql() + "' with params: \n"
				+ this.inParameterMapper);

		Map results = execute(getInParameterMapper());

		List list = (List) results.get("resultset");
		int total = ((Integer) results.get("totalSize")).intValue();

		PagingSet set = new PagingSet(list);
		set.setTotal(total);
		return set;
	}
}