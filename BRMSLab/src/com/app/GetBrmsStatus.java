package com.app;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.jasper.tagplugins.jstl.core.Out;

import com.cisco.dataconnect.process.Load;

import sun.java2d.pipe.OutlineTextRenderer;

/**
 * Servlet implementation class StoreIOSDeviceToken
 */
@WebServlet("/GetBrmsStatus")
public class GetBrmsStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetBrmsStatus() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String jsonString = IOUtils.toString(request.getInputStream());

		Load load = new Load();

		String message;
		try {
			message = load.search(jsonString);
		} catch (Throwable t) {
			message = "200 " + t.toString();
		}
		response.setContentType("text/plain");
		response.setContentLength(message.length());
		PrintWriter out = response.getWriter();
		out.println(message);
		out.flush();
		out.close();

		// doGet(request, response);
	}

}
