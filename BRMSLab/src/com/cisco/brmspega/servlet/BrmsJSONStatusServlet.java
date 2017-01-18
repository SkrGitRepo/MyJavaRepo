package com.cisco.brmspega.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class BrmsJSONStatusServlet
 */
@WebServlet("/BrmsJSONStatusServlet")
public class BrmsJSONStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BrmsJSONStatusServlet() {
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
		JsonObjectBuilder rootBuilder = Json.createObjectBuilder();
		JsonArrayBuilder domainArrayBuilder = Json.createArrayBuilder();
		JsonArrayBuilder appArrayBuilder = Json.createArrayBuilder();
		JsonObject root = null;

		String folderLoc = "/opt/brms/shared/scripts/";
		String reqURI = request.getRequestURI();
		System.out.println("URI is :" + reqURI);
		Collection<File> fileList = new ArrayList<File>();
		Iterator<File> fileListIterator = null;

		String[] uriParams = reqURI.split("\\/");

		if (uriParams.length > 1) {
			System.out.println("DOMAIN IS :" + uriParams[2]);

		}
		if (uriParams.length > 3) {
			System.out.println("APP/Host is: " + uriParams[3]);
		}

		String vdc = "NPRD1";
		String env = "PRD";
		JSONObject level1 = new JSONObject();
		JSONObject level2 = new JSONObject();
		JSONObject level3 = new JSONObject();
		Gson gson = new Gson();
		PrintWriter out = response.getWriter();
		out.print("<H3>REQUESTED URI : " + reqURI + "</H3><br/><br/>");
		out.print("<H3> GET LOCAL NAME :" + request.getLocalName() + "</H3>");
		out.print("<H3> GET LOCAL ADDRESS :" + request.getLocalAddr() + "</H3>");
		out.print("<H3> GET LOCAL PORT :" + request.getLocalPort() + "</H3>");
		// Collection<File> fileList = new ArrayList<File>();
		// Iterator<File> fileListIterator = null;
		final String fileFilterString = "brms_vm_cfg_" + vdc + "_*.txt";
		fileList = FileUtils.listFiles(new File(folderLoc), new WildcardFileFilter(fileFilterString), null);

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
							String[] tokens = line.split(",");
							System.out.println("TOKEN LENGTH ::" + tokens.length);
							System.out.println(tokens[0]);
							System.out.println(tokens[3]);

							System.out.println(tokens[0]);
							System.out.println(tokens[3]);
							System.out.println(tokens[9]);
							System.out.println(tokens[10]);

							String[] domainPath = tokens[9].split("/");
							// System.out.println("Domain is "+domainPath[1]);

							output = tokens[10] + "|" + domainPath[1] + "|" + tokens[0] + ":" + tokens[3];
							System.out.println("-----------------------------------------------");
							System.out.println("Final Output :");
							System.out.println(output);
							System.out.println("-----------------------------------------------");
							System.out.println("All Output :");
							String allOutput = tokens[0] + "|" + tokens[1] + "|" + tokens[2] + "|" + tokens[3] + "|"
									+ tokens[4] + "|" + tokens[5] + "|" + tokens[6] + "|" + tokens[7] + "|" + tokens[8]
									+ "|" + tokens[9] + "|" + tokens[10];
							System.out.println(allOutput);
							
							
							

							// for (Plant plant : plants) {
							// create a JSON object for each plant. The plants
							// will have the same names, but different values.
							JsonObjectBuilder hostBuilder = Json.createObjectBuilder();
							JsonObjectBuilder domainBuilder = Json.createObjectBuilder();
							JsonObject plantJson = hostBuilder.add("host",tokens[0])
									.add("port", tokens[3]).build();
							//domainArrayBuilder.add(plantJson).build();
							
							domainArrayBuilder.add(plantJson);
							// add the plant to our array of plants.
							//domainArrayBuilder.add(domainBuilder);
							root = rootBuilder.add(domainPath[1], domainArrayBuilder).build();
							// }

							// add the array of plants to a root JSON object.
							

							// try {
							// level3.put(tokens[3], tokens[0]);
							// level2.put(domainPath[1], level3);

							// and add that to outer
							// level1.put(tokens[10], level2.get("cpp-qa1"));
							// } catch (JSONException e) {
							// TODO Auto-generated catch block
							// e.printStackTrace();
							// }
							// System.out.println("Host : "+host+" with PORT
							// "+port+" not found.");
						}
					}
				}
				
				System.out.println("FINAL JSON OUTOUT IS :");
				System.out.println("=======================================");
				// System.out.println("KEEYYYY: "+level1.get("dev"));
				System.out.println(level1.toString());
				// System.out.println(gson.toJson(level1));
				// out.write(gson.toJson(level1));
				out.print("<br/><br/>");
				// Object json = mapper.readValue(level1.toString(),
				// Object.class);
				// System.out.println(mapper.prettyPrintingWriter((PrettyPrinter)
				// level1));
				// System.out.println(mapper._defaultPrettyPrinter().writeValueAsString(json));
				// gson = new GsonBuilder().setPrettyPrinting().create();
				// JsonParser jp = new JsonParser();
				// JsonElement je = jp.parse(level1.toString());
				// String prettyJsonString = gson.toJson(je);
				// out.write(prettyJsonString);
				System.out.println(root);
				out.print(root);
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
