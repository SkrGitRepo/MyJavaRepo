package com.journaldev.jdbc.datasource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class JDBCDataSourceExample
 */
@WebServlet("/JDBCDataSourceExample")
public class JDBCDataSourceExample extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JDBCDataSourceExample() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		Context ctx = null;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/MyDB");
			
			con = ds.getConnection();
			stmt = con.createStatement();
			
			rs = stmt.executeQuery("select userid,username from user_table;");
			
			PrintWriter out = response.getWriter();
            response.setContentType("text/html");
            out.print("<html><body><h2>USer Details</h2>");
            out.print("<table border=\"1\" cellspacing=10 cellpadding=5>");
            out.print("<th>User ID</th>");
            out.print("<th>User Name</th>");
            
            while(rs.next())
            {
                out.print("<tr>");
                out.print("<td>" + rs.getInt("userid") + "</td>");
                out.print("<td>" + rs.getString("username") + "</td>");
                out.print("</tr>");
            }
            out.print("</table></body><br/>");
            
            //lets print some DB information
            out.print("<h3>Database Details</h3>");
            out.print("Database Product: "+con.getMetaData().getDatabaseProductName()+"<br/>");
            out.print("Database Driver: "+con.getMetaData().getDriverName());
            out.print("</html>");
            
		}catch(NamingException e){
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				stmt.close();
				con.close();
				ctx.close();
			} catch (SQLException e) {
				System.out.println("Exception in closing DB resources");
			} catch (NamingException e) {
				System.out.println("Exception in closing Context");
			}
			
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
