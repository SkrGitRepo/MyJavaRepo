package com.cisco.brmspega.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;



/**
 * Servlet implementation class StoreIOSDeviceToken
 */
@WebServlet("/StoreIOSDeviceToken")
public class StoreIOSDeviceToken extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public List<String> iosDeviceTokenList = new ArrayList<String>();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StoreIOSDeviceToken() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String message = "Failed";
		String iosDeviceToken = IOUtils.toString(request.getInputStream());
        System.out.println("Device token to add:"+iosDeviceToken);
        //iosDeviceToken = "59f9b03cbcf269449e0994aaf449f9505ff658cd557271f79b739fd729e21103";
        //iosDeviceToken = "dc09fe83c5f68ace0d97491e100f170473199cfa7dba2417180b65fe9ac3b76e";
        try {
        	if (iosDeviceToken != null) {
    			message = storeIOSDeviceToken(iosDeviceToken);
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		response.setContentType("text/plain");
		response.setContentLength(message.length());
		PrintWriter out = response.getWriter();
		out.println(message);
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public String storeIOSDeviceToken(String deviceToken) throws IOException {
		String folderLoc = "/opt/brms/shared/scripts";//PropertyLoader.getInstance().getProperty("monitor_config_base");
		String message = "Device token could not be added"+deviceToken;
		File token_lists_file = new File(folderLoc+"/ios_device_token_list.txt");
		if (!token_lists_file.getParentFile().exists()) {
			token_lists_file.getParentFile().mkdir();
		}
		if (!token_lists_file.exists()) {
			try {
				token_lists_file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		List<String> deviceTokenEntries = null;
		
		if (token_lists_file.exists()) {
			List<String> newTokenList = new ArrayList<String>();
			deviceTokenEntries = FileUtils.readLines(token_lists_file);
			if(deviceTokenEntries.isEmpty()) {
				if (deviceToken != null) {
					deviceTokenEntries.add(deviceToken);
					System.out.println("Added first device token"+deviceToken);
					message = "Device token added :"+deviceToken;
				}
			} else {
				for (String entry : deviceTokenEntries) {
					if (null == entry || entry.trim().equalsIgnoreCase("")) {
						if (deviceToken != null) {
							deviceTokenEntries.add(deviceToken);
							System.out.println("Token list was empty ...Added device token :" + deviceToken);
							message = "Device token added :"+deviceToken;
						}
						continue;
					} else if (entry.equalsIgnoreCase(deviceToken)) {
						System.out.println("Toekn already exist in the file :"+ deviceToken);
						message = "Device token pre-exist :"+deviceToken;
						continue;
					} else if (!deviceTokenEntries.contains(deviceToken)){
						System.out.println("New token added to the device token list :"+deviceToken);
						//tokenEntries.add(deviceToken);
						if(!newTokenList.contains(deviceToken))	{
							newTokenList.add(deviceToken);
							message = "Device token added :"+deviceToken;
						}
					}
				}
			}
			deviceTokenEntries.addAll(newTokenList);
			FileUtils.writeLines(token_lists_file, deviceTokenEntries);
		}
		return message;
	}
	
}
