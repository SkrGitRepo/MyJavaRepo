package com.cisco.brmspega.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.json;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.codehaus.jackson.PrettyPrinter;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class DomainStatusJSONServlet
 */
@WebServlet("/DomainStatusJSONServlet")
public class DomainStatusJSONServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DomainStatusJSONServlet() {
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
		// response.getWriter().append("Served at:
		// ").append(request.getContextPath());
		PrintWriter out = response.getWriter();
		String folderLoc = "/opt/brms/shared/scripts/";
		String reqURI = request.getRequestURI();
		System.out.println("URI is :" + reqURI);
		Collection<File> fileList = new ArrayList<File>();
		Iterator<File> fileListIterator = null;
		Map<String, List<String>> map1 = new HashMap<String, List<String>>();
		String searchDomain = null;
		String searchApp = null;
		String[] uriParams = reqURI.split("\\/");

		if (uriParams.length == 4) {
			searchDomain = uriParams[3];
			out.println("<H3>URI LENGHT IS :" +uriParams.length+"</H3>");
			out.print("<H3>DOMAIN IS :" + uriParams[3]+"</H3>");
		} else if (uriParams.length == 5) {
			searchDomain = uriParams[3];
			searchApp = uriParams[4];
 			out.print("<H3> APP/Host is: " + uriParams[4]+"</H3>");
			
		} else if (uriParams.length == 3){
			searchDomain = "All";
			out.print("<H3> ALL: " + uriParams[2]+"</H3>");
		}

		String vdc = "PRD1";
		String env = "PRD";
		JSONObject level1 = new JSONObject();
		JSONObject level2 = new JSONObject();
		JSONObject level3 = new JSONObject();
		Gson gson = new Gson();
		
		out.print("<H3>REQUESTED URI : " + reqURI + "</H3><br/><br/>");
		out.print("<H3> GET LOCAL NAME :" + request.getLocalName() + "</H3>");
		out.print("<H3> GET LOCAL ADDRESS :" + request.getLocalAddr() + "</H3>");
		out.print("<H3> GET LOCAL PORT :" + request.getLocalPort() + "</H3>");
		// Collection<File> fileList = new ArrayList<File>();
		// Iterator<File> fileListIterator = null;
		final String fileFilterString = "brms_vm_cfg_" + vdc + "_*.txt";
		fileList = FileUtils.listFiles(new File(folderLoc), new WildcardFileFilter(fileFilterString), null);
		// String fileName=
		// "C:/opt/brms/shared/scripts"+"/brms_vm_cfg_"+vdc+"_"+env+".txt";
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, ArrayList<String>> items = new HashMap<String, ArrayList<String>>();
		ArrayList<String> al = new ArrayList<String>();

		// System.out.println("Config File :"+fileName);
		// "C:/opt/brms/shared/scripts/brms_vm_cfg_PRD1_PRD.txt"
		// TODO Auto-generated method stub

		// File file = new File(fileName);
		File file;
		// host = "brms-prd1-13";
		// port ="7005";
		String output = null;
		fileListIterator = fileList.iterator();
		while (fileListIterator.hasNext()) {
			file = (File) fileListIterator.next();
			System.out.println("-----------------------------------------------");
			System.out.println("Read config file");
			System.out.println("-----------------------------------------------");
			System.out.println(file.getName());
			if (file.exists()) {
				List<String> lines = FileUtils.readLines(file);
				for (String line : lines) {
					if (!line.contains("CONTEXT")) {
						if (!line.contains("#")) {
							//if(line.contains(searchDomain)) {
							String[] tokens = line.split(",");
							System.out.println("TOKEN LENGTH ::" + tokens.length);
							System.out.println(tokens[0]);
							System.out.println(tokens[3]);

							System.out.println(tokens[0]);
							System.out.println(tokens[3]);
							System.out.println(tokens[9]);
							System.out.println(tokens[10]);

							String[] domainPath = tokens[9].split("/");
							ArrayList<String> appList = new ArrayList<String>();
							if (domainPath.length == 3)
								appList.add(domainPath[2]);
							else 
								appList.add("NA");
							// System.out.println("Domain is "+domainPath[1]);

							output = tokens[10] + "|" + domainPath[1] + "|" + tokens[0] + ":" + tokens[3];
							System.out.println("-----------------------------------------------");
							//System.out.println("Final Output :");
							//System.out.println(output);
							System.out.println("-----------------------------------------------");
							System.out.println("All Output :");
							String allOutput = tokens[0] + "|" + tokens[1] + "|" + tokens[2] + "|" + tokens[3] + "|"
									+ tokens[4] + "|" + tokens[5] + "|" + tokens[6] + "|" + tokens[7] + "|" + tokens[8]
									+ "|" + tokens[9] + "|" + tokens[10];
							System.out.println(allOutput);
							Date currentTimeStamp =  new Date(System.currentTimeMillis());
							
							try {
								if ((domainPath[1].equals(searchDomain)) && (appList.isEmpty())) {
									level3.put(tokens[0]+"_"+tokens[3],tokens[3]);
									level3.put("STATUS", "Up");
									level3.put("DATE_TIME", currentTimeStamp.toString());
									level2.put("DOMAIN",domainPath[1]);
									//level2.put("APPs",appList);
									//level2.put("Host:PORT", level3);
									level2.put(domainPath[1], level3);
									// and add that to outer
									//level1.put(tokens[10], level2);
								} else if ((domainPath[1].equals(searchDomain)) && (searchApp == null)) {
									level3.put(tokens[0],tokens[3]);
									level3.put("STATUS", "Up");
									level3.put("APPS",appList);
									level3.put("DATE_TIME", currentTimeStamp.toString());
									//level2.put("DOMAIN",domainPath[1]);
									//level2.put("APPs",appList);
									//level2.put("Host:PORT", level3);
									level2.put(domainPath[1], level3);
									// and add that to outer
									//level1.put(tokens[10], level2);
								} else if ( (domainPath[1].equals(searchDomain))  && (appList.contains(searchApp) && (!appList.isEmpty()))) {
									level3.put(tokens[0],tokens[3]);
									level3.put("STATUS", "Up");
									level3.put("DATE**TIME", currentTimeStamp.toString());
									//level2.put("DOMAIN",domainPath[1]);
									level3.put("APPS",appList);
									//level2.put("Host:PORT", level3);
									level2.put(domainPath[1], level3);
								}else if (searchDomain.contentEquals("All"))  {
									//al.add(tokens[0]);
									//items.put(domainPath[1], al);
									//if(items)
									//items.get(domainPath[1]).add(tokens[0]);
									//level2.put("DOMAIN",level3);
									if(level2.has(domainPath[1])) {
										level3.put(tokens[0],tokens[3]);
										level3.put("STATUS", "Up");
										level3.put("DATE||TIME", currentTimeStamp.toString());
										level2.put(domainPath[1], level3);
									} else { 
										level3.put(tokens[0],tokens[3]);
										level3.put("STATUS", "Up");
										level3.put("DATE||TIME", currentTimeStamp.toString());
										level2.put(domainPath[1], level3);
									}
									//level1.put(tokens[10], level2);
								}
								level1.put(tokens[10], level2);
								// level1.put(tokens[10],level2.get("cpp-qa1"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// System.out.println("Host : "+host+" with PORT
							// "+port+" not found.");
						}
					}
					//}
				}
				System.out.println("FINAL JSON OUTOUT IS :");
				System.out.println("=======================================");
				// System.out.println("KEEYYYY: "+level1.get("dev"));
				if (level1 != null) {
					System.out.println(level1.toString());
					// System.out.println(gson.toJson(level1));
					// out.write(gson.toJson(level1));
					out.write("<br/><br/>");
					Object json = mapper.readValue(level1.toString(), Object.class);
					// System.out.println(mapper.prettyPrintingWriter((PrettyPrinter)
					// level1));
					// System.out.println(mapper._defaultPrettyPrinter().writeValueAsString(json));
					gson = new GsonBuilder().setPrettyPrinting().create();
					JsonParser jp = new JsonParser();
					JsonElement je = jp.parse(level1.toString());
					String prettyJsonString = gson.toJson(je);
					out.write(prettyJsonString);
					System.out.println();
					//out.write(level1.toString());
				} else {
					out.write("<H3>NOT MATCHING DATA FOUND</H3>");
				}
				// out.write("<br/><br/>"+level1.get("stage"));
				out.flush();
				out.close();

			} else {
				System.out.println("File does not exist");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
