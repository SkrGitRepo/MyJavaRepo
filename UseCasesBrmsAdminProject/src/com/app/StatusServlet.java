package com.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class StatusServlet
 */
@WebServlet("/StatusServlet")
public class StatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StatusServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String nodeMonStatus = request.getParameter("monstatus");
		String nodeLifecycle= request.getParameter("lifecycle");
		String nodeName= request.getParameter("node");
		
		String array[] = nodeName.split(":", -1); 
		
		String hostName = array[0];
		String hostPort = array[1];
		
		String hostRecordFile = "C:/EAJavauseCasePrj/HostManagement/"+nodeLifecycle+"_host_file.txt";
		
		PrintWriter out = response.getWriter();
		
		response.setContentType("text/html");
		out.println("<html><head></head>");
		out.println("<body><center>");

		if (nodeMonStatus.equalsIgnoreCase("disabled")) {
			
			boolean isHostDisbled = disableHostMonitoring(hostRecordFile, nodeName);
			if (isHostDisbled) {
				
				out.println("<br/><br/><br/><br/><H3><font color='green'>Disabled Succesfully</font><H3><br/>"
						+ "<div  align='center'><table border='1'><tr><h3>Current Monitoring status </h3></tr><tr><th>Lifecycle</th><th>HostName</th><th>Port No</th><th>Monitoring</th></tr>");
				out.println("<tr><td>"+nodeLifecycle+"</td><td>"+hostName+"</td><td>"+hostPort+"</td><td><font color='red'>"+nodeMonStatus
						+"</font></td></tr></table></div>");
				out.println("<br/>");
			} else {
				out.println("<br/><br/><p><B>Monitoring on Lifecycle :</B>"+nodeLifecycle +"</p><p><B> for Host: </B>"+hostName
						+"</p><p><B> is failed to :</B>"+nodeMonStatus.toUpperCase()+"</p>" );
				out.println("<br/>");
			}
			
		} else {
			out.println("<br/><br/><br/><br/><div  align='center'><table border='1'><tr><h3>Current Monitoring status </h3></tr><tr><th>Lifecycle</th><th>HostName</th><th>Port No</th><th>Monitoring</th></tr>");
			out.println("<tr><td>"+nodeLifecycle+"</td><td>"+hostName+"</td><td>"+hostPort+"</td><td><font color='green'>"+nodeMonStatus+"</font></td></tr></table></div>");
			out.println("<br/>");
		}
		
		out.println("<p><a href='http://localhost:8088/HostManagement/'><font  color='#CC0066'><H3>Back to Home</H3></a></p>");
		out.println("</body></html>");
		
	}

	public boolean disableHostMonitoring(String hostRecordFile, String hostToDisabled) {
		
		boolean isHostDisbled = false;

        try {

            File inFile = new File(hostRecordFile);

            if (!inFile.isFile()) {
                System.out.println("Parameter is not an existing file");
                return isHostDisbled;
            }

            // Construct the new file that will later be renamed to the original
            // filename.
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader(hostRecordFile));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String hostRecord = null;
            Pattern regexp = Pattern.compile(hostToDisabled);
            Matcher matcher = regexp.matcher("");
            // Read from the original file and write to the new
            // unless content matches data to be removed.
            int count = 0;
            while ((hostRecord = br.readLine()) != null) {
            	matcher.reset(hostRecord); //reset the input
                if (!matcher.find()) {
                	System.out.println("Host " +hostToDisabled+" monitoring is disabled now.");
                    pw.println(hostRecord);
                    pw.flush();
                    count += count;
                }
            }
            pw.close();
            br.close();

            // Delete the original file
            if (!inFile.delete()) {
                System.out.println("Could not delete file");
                return isHostDisbled;
            }

            // Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(inFile))
                System.out.println("Could not rename file");
            
            if (count >= 0) {
            	isHostDisbled = true;
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		return isHostDisbled;
    }


}
