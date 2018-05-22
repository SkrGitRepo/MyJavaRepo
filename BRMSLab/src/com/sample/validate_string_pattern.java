package com.sample;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class validate_string_pattern {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "https://ibpmadm.cisco.com/bcs/as/PRRestService/CiscoSample/Services/eman";
		//url="https://www.google.com";
		
		if (url.startsWith("https://ibpm") || url.startsWith("https://ibpmadm") || url.startsWith("https://ibpmapi")) {
			System.out.println("url is ibpm url");
		} else {
			System.out.println("url is not ibpm url");
		}

		if (url.contains("ibpm") || url.contains("ibpmadm") || url.contains("ibpmapi")) {
			System.out.println("URL cotnains ibpm");
		} else {
			
			System.out.println("url does not contain ibpm ");
			
		}
		
		
		String logPath = "/opt/brms/shared/logs/brms-nprd2-stg13/bcs-7032/";
		System.out.println(logPath.replace("/shared", ""));
		
		
		
		String hostName="brms-nprd2-stg12";
		String portNum="7024";
		String sCurrentLine =  "Sun Mar 25 08:09:48 EDT 2018	Kill WL	brms-nprd2-stg12	7024	malganga	Weekly Maintenance";
		
		Pattern hostPattern = Pattern.compile("\\b"+hostName+"\\b");
		Matcher hostMatch =hostPattern.matcher(sCurrentLine);
		//System.out.println("Host matches: " +hostMatch.find());

		Pattern portPattern = Pattern.compile("\\b"+portNum+"\\b");
		Matcher portMatch = portPattern.matcher(sCurrentLine);
		//System.out.println("Port matches: " + portMatch.find());
		
		if ( hostMatch.find() && portMatch.find() ) {
			System.out.println("Matching History found: " +hostName);
			//logHistoryList.add(tokens);
		} else {
			System.out.println("No Matching History found: " +hostName);
		}
		
		
		
		/*Pattern hostPattern = Pattern.compile("\\b"+hostName+"\\b");
		Matcher hostMatch =hostPattern.matcher(sCurrentLine);
		
		if(hostMatch.find())
			System.out.println("Host matches: " +hostMatch.find());

		Pattern portPattern = Pattern.compile("\\b"+portNum+"\\b");
		Matcher portMatch = portPattern.matcher(sCurrentLine);
		
		if(portMatch.find())
			System.out.println("Port matches: " + portMatch.find());
		
		//if(sCurrentLine.contains(hostName) && sCurrentLine.contains(portNum)){ 
		if ( hostMatch.find() && portMatch.find() ) {
			System.out.println("Matching History found: " +hostName);
			//logHistoryList.add(tokens);
		}
		*/
		

	}

}
