package com.cisco.brmspega.ping.util;

import java.sql.Connection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import oracle.jdbc.pool.OracleDataSource;

public class DbConnection {
	public static Connection getConnection(String container, String jndiDsName) throws Exception {
		Context ctx = null;
		Connection conn = null;
		DataSource ds = null;
		InitialContext initCtx = new InitialContext();
		if ((container != null) && (container.equalsIgnoreCase("LOCAL"))) {
			OracleDataSource ods = new OracleDataSource();
			ods.setURL("jdbc:oracle:oci8:@sefdev");
			ods.setUser("brm");
			ods.setPassword("brm123");
			ds = ods;
		} else if ((container != null) && (container.equalsIgnoreCase("WLLOCAL"))) {
			OracleDataSource ods = new OracleDataSource();
			ods.setURL("jdbc:oracle:thin:@dbgen-dev1-vm-01.cisco.com:1521:sefdev");
			ods.setUser("brm");
			ods.setPassword("brm123");
			ds = ods;
		} else if ((container != null) && (container.equalsIgnoreCase("TOMCAT"))) {
			ctx = (Context) initCtx.lookup("java:/comp/env");
			ds = (DataSource) ctx.lookup(jndiDsName);
		} else {
			ds = (DataSource) initCtx.lookup(jndiDsName);
		}
		conn = ds.getConnection();
		return conn;
	}
}
