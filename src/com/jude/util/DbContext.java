package com.jude.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;

public class DbContext {
	private static ApplicationContext context;

	public static void setContext(ApplicationContext applicationContext) {
		context = applicationContext;
	}

	public static ApplicationContext getContext() {
		if (context == null) {
			context = new ClassPathXmlApplicationContext("applicationContext.xml");
		}
		return context;
	}

	public static JdbcTemplate getJdbcTemplate() {
		return (JdbcTemplate) getContext().getBean("jdbcTemplate");
	}

	public static DataSource getDataSource() {
		return (DataSource) getContext().getBean("dataSource");
	}

	public static PlatformTransactionManager getTm() {
		return (PlatformTransactionManager) getContext().getBean("transactionManager");
	}

	public static boolean isMySQL() {
		return "MySQL".equalsIgnoreCase(getDatabaseProductName());
	}

	public static boolean isSQLServer2005() {
		return "Microsoft SQL Server2005".equalsIgnoreCase(getDatabaseProductName());
	}

	public static String getDatabaseProductName() {
		Connection conn = null;
		DataSource ds = getDataSource();
		try {
			conn = DataSourceUtils.getConnection(ds);
			DatabaseMetaData dbmd = conn.getMetaData();
			String str = dbmd.getDatabaseProductName();

			return str;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceUtils.releaseConnection(conn, ds);
		}
		return null;
	}
}