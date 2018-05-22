package com.sample;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.List;
public class WordMatcher {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String cmd = "id";
		cmd = cmd.contains(" ") ? cmd.split(" ")[0] : cmd;
		cmd = cmd.toLowerCase();
		if (cmd.matches("rm|mv|reboot|yum|rpm|id")) {
			System.out.println("Restricted command :"+cmd);
		} else {
			System.out.println("Un-Restricted command :"+cmd);
		}
		//https://ibpm.cisco.com/prd1/brmsadmin/getFile?fN=/opt/brms/shared/logs/brms-prd4-adm1/adm1-7005/../../../../apps/brms/.ssh/id_rsa
		String matSen = "/opt/brms/shared/logs/brms-prd4-adm1/adm1-7005/**/**/hello.txt/.././apps/brms/.ssh/id_rsa";
		//Pattern matchPattern = Pattern.compile("(?!id|.sh|.py|..).*$");
		matSen = "/opt/brms/shared/logs/brms-test-5/test-7030/weblogic/WLAdminServer-09-26-2017-18.40.out/../../../hello.sh";
		
		
		
		
		Pattern matchPattern = Pattern.compile("(?:(?<!\\.)\\.\\.(?:\\.)|\\.{2,})");
		Matcher actionMatch = matchPattern.matcher(matSen);
		if (actionMatch.find()) {
			 System.out.println(" String found "+actionMatch.find());
		} else {
			System.out.println("not found");
		}
		
		Pattern fileMatchPattern = Pattern.compile("(?:.py|.sh|.jpg)$|(?:.py[?]|.sh[?]|.jpg[?])");
		//String fileRgx = "^(.+?)(\\.tar)?\\.gz";
		String filePatMatch = "/opt/brms/shared/logs/brms-prd4-adm1/adm1-7005/**/**/hello.py/.././apps/brms/.ssh/id_rsa";
		//filePatMatch = "/opt/helll/python.py.sh/hell/";
		Matcher fileMatch = fileMatchPattern.matcher(filePatMatch);
		String ext = "txt";
		if (filePatMatch.matches(".*\\b\\.py\\b.*|.*\\b\\.sh\\b.*")) {
		//if(fileMatch.find()) {
			System.out.println("Match found:");
		} else {
			System.out.println(" No match found");
		}
		
		
		
		String server_path = "/opt/brms/shared/tc/brms-test-1-eatest-8084";
		String server_type = "Weblogic";
		
		if (server_path.contains("tc")) {
			server_type = "Tomcat";
		} else {
			server_type = "Weblogic";
		}
		
		System.out.println("SERVER TYPE is :"+server_type);
		

		String  vm_cfg = "brms-nprd2-poc1,/opt/brms/shared/tc/brms-nprd2-poc1-prweb7-8081,8443,8005,/opt/brms/shared/tc/brms-nprd2-poc1-prweb7-8081/prcache,-Dcom.sun.management.jmxremote.port=8300,-Xms4000m,/opt/brms/shared/logs/brms-nprd2-poc1/prweb7-8081,-Xmx10000m,/prweb7,poc,pegaconfig/prweb7,brms_db_cfg_NPRD2_POC.txt,brms_app_cfg_NPRD2_POC.txt,domain,,wl";
		String [] vm_cfg_arr = vm_cfg.split(",");
		List<String> VALUES = Arrays.asList(vm_cfg_arr);


		if (VALUES.contains("tc")) {
		     System.out.println("Contains");
		} else {
		    System.out.println("Not contains");
		}
		
		
		String alphnumeric = "prsysmgmt731.war";
		String numberOnly= alphnumeric.replaceAll("[^0-9]", "");
		System.out.println(numberOnly);
	}

}
