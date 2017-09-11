package com.cisco.brmspega.servlet;

import java.io.BufferedWriter;

import java.io.FileWriter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.nio.file.StandardCopyOption.*;

/**
 * Servlet implementation class saveFile
 */

public class saveFile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public saveFile() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		
		
		String strURLPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();//ParamUtil.getURLPathFromSession(request);
		String fileContent = request.getParameter("fileContent");
		String fileName = request.getParameter("fileName");

		
		Calendar calendar = new GregorianCalendar();
		   String am_pm;
		   int hour = calendar.get(Calendar.HOUR);
		   int minute = calendar.get(Calendar.MINUTE);
		   int second = calendar.get(Calendar.SECOND);
		   if(calendar.get(Calendar.AM_PM) == 0)
		      am_pm = "AM";
		   else
		      am_pm = "PM";
		   String CT = hour+"_"+ minute +"_"+ second +"_"+ am_pm;
		
		String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
		Path oldFile = Paths.get(fileName);
		Files.move(oldFile, oldFile.resolveSibling(fileName+ "_" + timeStamp +".old"));
		
		System.out.println(" File Content to Save :" + fileContent);
		System.out.println(" File to save onto :"+fileName);

		String saveToFile = fileName;

		BufferedWriter bw = null;
		FileWriter fw = null;
		boolean fileSaved = false;

		try {

			String content = "This is the content to write into file\n";

			fw = new FileWriter(saveToFile);
			bw = new BufferedWriter(fw);
			bw.write(fileContent);

			fileSaved = true;
			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

		
		if(fileSaved) {
			response.sendRedirect(strURLPath+"/jsp/FileEditor.jsp?fSave=Y&inFile="+fileName);
		} else {
			response.sendRedirect(strURLPath+"/jsp/FileEditor.jsp?fSave=N&inFile="+fileName);
		}
	}
}

