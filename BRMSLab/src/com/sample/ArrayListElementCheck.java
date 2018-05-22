package com.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayListElementCheck {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String dev_new_ping_domain_list = "csc,csw";
		List<String> devDomainlist = new ArrayList<String>(Arrays.asList(dev_new_ping_domain_list.split(",")));
		
		String arstring = "sumkuma2,jbanerj";
	   
		
		String[] arrayString = arstring.split(",");
		
		//String[] arrayString = {"sumkuma2","jbanerj"};
		
		
		for (String string : arrayString) {
			System.out.println("Name"+string);
		}
		
		
//		String domainName = "/csw";
//		domainName = domainName.substring(domainName.lastIndexOf("/") + 1);
//		if(devDomainlist.contains(domainName)) {
//			System.out.println("List contains domain name:"+domainName);
//		} else {
//			System.out.println("List does not contains Domain NAME ::"+domainName);
//		}
//		

	}

}
