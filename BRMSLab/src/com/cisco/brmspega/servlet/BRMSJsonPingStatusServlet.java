package com.cisco.brmspega.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.jasper.tagplugins.jstl.core.ForEach;

import com.cisco.brmspega.bundles.BrmsPropertyLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sample.utility.BrmsJsonUtil;

/**
 * Servlet implementation class BRMSJsonPingStatusServlet
 */
@WebServlet("/BRMSJsonPingStatusServlet")
public class BRMSJsonPingStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();

		String reqURI = request.getRequestURI();
		String searchDomain = null;
		String searchApp = null;
		String searchLifecycle =  "all";
		String[] uriParams = reqURI.split("\\/");
		response.setContentType("application/json");
		
		if (uriParams.length == 5) {
			searchDomain = uriParams[4];
			searchLifecycle =  uriParams[3];
		} else if (uriParams.length == 6) {
			searchDomain = uriParams[4];
			searchApp = uriParams[5];
			searchLifecycle =  uriParams[3];
			
		} else if (uriParams.length == 4) {
			searchDomain = null;
			searchApp = null;
			searchLifecycle =  uriParams[3];
		}
		
		/*if (uriParams.length == 4) {
			searchDomain = uriParams[3];
		} else if (uriParams.length == 5) {
			searchDomain = uriParams[3];
			searchApp = uriParams[4];
		} else if (uriParams.length == 3) {
			searchDomain = null;
			searchApp = null;
			//searchDomain = "cvc";
			//searchApp = "score";
		} */
		
		String jsonOutput =  BrmsJsonUtil.getJson(searchLifecycle, searchDomain, searchApp);
		out.println(jsonOutput);
		
		out.flush();
		out.close();
		
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



