package com.journaldev.jdbc.datasource;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.journaldev.jdbc.datasource.JDBCPGExample.Blog;

public class JDBCExample {

	public static void main(String[] argv) {

		System.out.println("-------- PostgreSQL "
				+ "JDBC Connection Testing ------------");

		try {

			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
			return;

		}

		System.out.println("PostgreSQL JDBC Driver Registered!");

		Connection connection = null;

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/javadb", "postgres",
					"postgres");
			
			 try 
			    {
			      Statement st = connection.createStatement();
			      ResultSet rs = st.executeQuery("SELECT id,url,name FROM link ORDER BY id");
			      while ( rs.next() )
			      {
			        
			        int id       = rs.getInt("id");
			        String url   = rs.getString ("url");
			        String siteName = rs.getString ("name");
			        System.out.println(" ID"+id+" URL:"+url +" siteName:"+siteName);
			        
			      }
			      rs.close();
			      st.close();
			    }
			    catch (SQLException se) {
			      System.err.println("Threw a SQLException while getting the list from table.");
			      System.err.println(se.getMessage());
			    }

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;

		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
	}

}