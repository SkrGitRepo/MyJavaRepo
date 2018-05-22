package com.cisco.brmspega.ping.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cisco.brmspega.ping.util.DBQueryClient;


/**
 * Servlet implementation class PingIgniteServlet
 */
@WebServlet("/PingIgniteServlet")
public class PingIgniteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PingIgniteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		PrintWriter ps = response.getWriter();
		response.reset();
		String container = "Tomcat";
		String jndiDsName = "jdbc/PegaRULES";
		String jndiDsAliasName = "PegaRules"; 
		String query = "select sysdate from dual";
		String pingInd = "y";
		String rowCount = "0";
		

		int rowCnt = 0;
		if (rowCount != null && !rowCount.equals("")) {
			rowCnt = Integer.parseInt(rowCount);
		}
		// Firing the query
		Connection con = null;
		Statement stat = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		try {
			
			String dbConRes = DBQueryClient.queryDB(container, jndiDsName, jndiDsAliasName , query);
			
			if (dbConRes == null) {
				ps.write("Server Down");
			} else if (dbConRes.contains("Health good")){
				ps.write("EMAN OK!");
			} else if (dbConRes.contains("Bad Health")){
				ps.write("Server Down");
			}			

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ps.write("<span class='pageText'>" + "Exception occured : " + ex.getMessage() + "</span>");
		} finally {
			try {
				if (stat != null)
					stat.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ps.flush();
			ps.close();
		}
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
