package com.sample;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.sun.org.apache.bcel.internal.generic.AALOAD;

import jdk.nashorn.internal.parser.TokenKind;

public class CountUniqueUsers {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		final String USER_ACTIVITY_LOG_PATH = "/users/sumkuma2/Desktop/Onramp_All_user";
		final String serverToolsConfigBase = "/opt/brms/shared/scripts";
		
		ArrayList<String> allUniqueUsers = new ArrayList<String>(); 
		ArrayList<String> allUsers = new ArrayList<String>();

		String fileName = USER_ACTIVITY_LOG_PATH + "/BCS_PROD_ALL_USERS_CSV_onramp_user_export-140244242-20180516205303_1.csv";
		// System.out.println("Reading config file :"+fileName);
		File file = new File(fileName);
		String output = null;

		if (file.exists()) {
			List<String> lines = FileUtils.readLines(file);
			for (String line : lines) {
				if (!line.contains("PARENT_RESOURCE_PATH")) {
					if (!line.isEmpty()) {
						String[] tokens = line.split(",");
						System.out.println("LINE length" + tokens.length);
						
						if (tokens.length > 3 && !allUniqueUsers.contains(tokens[2]) ) {
							System.out.println("Unqiue User :"+tokens[2]);
							allUniqueUsers.add(tokens[2]);
						}
						
						if (tokens.length > 3 ) {
							
							//System.out.println("Unqiue User :"+tokens[2]);
							allUsers.add(tokens[2]);
						}
						
						
						/*if (tokens.length > 3 && !allUniqueUsers.contains(tokens[2]) && ( tokens[1].contains("PROD") || tokens[1].contains("Production"))) {
							
							System.out.println("Unqiue User :"+tokens[2]);
							allUniqueUsers.add(tokens[2]);
						}
						
						if (tokens.length > 3 && ( tokens[1].contains("PROD") || tokens[1].contains("Production"))) {
							
							System.out.println("Unqiue User :"+tokens[2]);
							allUsers.add(tokens[2]);
						}
*/						
					/*	if(!allUniqueUsers.isEmpty() && !allUniqueUsers.contains(tokens[2]) && ){
							System.out.println("Unqiue User :"+tokens[2]);
							allUniqueUsers.add(tokens[2]);
						}*/
						

					} else {
						System.out.println("Matching config file does not exist : " + fileName);
					}

				}

			}

		}
		
		System.out.println("ALL UNIQUE: "+allUniqueUsers.size());
		for (Iterator iterator = allUniqueUsers.iterator(); iterator.hasNext();) {
			String user = (String) iterator.next();
			System.out.println( "Uniqueuser is: "+user);
			
		}
		
		System.out.println("ALL USERS: "+allUsers.size());

	}
	}