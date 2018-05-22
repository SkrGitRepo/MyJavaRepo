package com.cisco.brmspega.ping.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.cisco.brmspega.ping.util.DbConnection;;

public class DBQueryClient {

	public static String queryDB(String container, String jndiDsName, String jndiDsAliasName, String query) {
		String pingInd = "y";
		Connection con = null;
		Statement stat = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		String dbRes = "Bad Health";
		try {
			long stTime = new java.util.Date().getTime();
			con = DbConnection.getConnection(container, jndiDsName);
			stat = con.createStatement();
			rs = stat.executeQuery(query);
			long timeTaken = (new java.util.Date().getTime() - stTime);
			rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			// System.out.println(" Query Column Count is :"+colCount);
			if (pingInd != null && pingInd.equalsIgnoreCase("Y") && colCount >= 0) {
				dbRes = jndiDsAliasName + " Health good. Time Taken is " + timeTaken + " milliseconds";
			} else {
				dbRes = "Bad Health";
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			dbRes = "Bad Health";
		} finally {
			try {
				if (stat != null)
					stat.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				dbRes = "Bad Health";
			}
		}

		return dbRes;
	}

}
