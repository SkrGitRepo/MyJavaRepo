package com.cisco.csc.sdk.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;

import com.cisco.csc.CSCUploadHandler;
import com.cisco.csc.common.Constants;
import com.cisco.csc.common.UserAuthentication;

public class TestService_Upload {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		HashMap inputParams = new HashMap();
		OutputStream out = null;
		InputStream inputStream = null;
		
		
		
		try{


			UserAuthentication authData = new UserAuthentication();
			
			authData.setUseName("csca2a.gen");
			authData.setPassWord("Y3NjaUBhMmE=");
			String fileName =  "CSC_Upload Call.txt";
			//System.setProperty("cisco.life", "dev");
			
			
			/*inputParams.put(Constants.TAG_TENANT_CODE, "");
			inputParams.put(Constants.TAG_TENANT_SOURCE_KEY, "");
			inputParams.put(Constants.TAG_FILE_NAME, "");
			inputParams.put(Constants.TAG_TENANT_SUBSCRIPTION_KEY, "");
			inputParams.put(Constants.TAG_TENANT_SUB_CODE, ""); 
			inputParams.put(Constants.TAG_UPLOADED_BY_EMAIL, "");
			inputParams.put(Constants.TAG_FILE_CATEGORY, "");
			inputParams.put(Constants.USER_ID, "");
			inputParams.put(Constants.USER_EMAILID, "");			
			inputParams.put(Constants.VISIBILITY_FLAG, "");
			inputParams.put(Constants.TAG_COMMIT_FLAG, "");
			inputParams.put(Constants.INVOCATION_METHOD, "");*/
			
			/*inputParams.put("tenantCode", "TAC");
			inputParams.put("tenantSourceKey","680136812");
			inputParams.put("fileName", fileName);
			inputParams.put("tenantSubscriptionKey","A62E12FB3784583F82220872A7BE7597");
			inputParams.put("tenantSubCode","JJVG6PT8SUID");
			inputParams.put("uploadedByEmail","jabanerj@cisco.com");
			inputParams.put("userId","jabanerj");
			inputParams.put("userEmailId","jabanerj@cisco.com");*/
			
			
			inputParams.put(com.cisco.csc.common.Constants.TAG_TENANT_CODE, "TAC");
			inputParams.put(com.cisco.csc.common.Constants.TAG_TENANT_SOURCE_KEY, "680136812"); //600000011
			inputParams.put(com.cisco.csc.common.Constants.TAG_FILE_NAME, fileName);
			inputParams.put(com.cisco.csc.common.Constants.TAG_TENANT_SUBSCRIPTION_KEY, "A62E12FB3784583F82220872A7BE7597");
			inputParams.put(com.cisco.csc.common.Constants.TAG_TENANT_SUB_CODE, "JJVG6PT8SUID"); //SCM
			inputParams.put(com.cisco.csc.common.Constants.TAG_UPLOADED_BY_EMAIL, "jabanerj@cisco.com"); //shakalia@cisco.com
			inputParams.put(com.cisco.csc.common.Constants.USER_ID, "jabanerj"); //shakalia
			inputParams.put(com.cisco.csc.common.Constants.USER_EMAILID, "jabanerj@cisco.com"); 
		//inputParams.put(Constants.TAG_UPLOADED_BY_PROGRAM, "pganne");
			inputParams.put(com.cisco.csc.common.Constants.TAG_FILE_CATEGORY, "web_uploaded"); 
		// inputParams.put(com.cisco.csc.common.Constants.VISIBILITY_FLAG, "");
			inputParams.put(com.cisco.csc.common.Constants.TAG_COMMIT_FLAG, "yes");
			inputParams.put(com.cisco.csc.common.Constants.INVOCATION_METHOD, "test_sdk");

			
			/*Code to upload file */
			 
			File myFile = new File("C:\\Users\\sumkuma2\\Desktop\\csc_uploader\\CSC_Upload Call.txt");
			//C:\\Users\\jabanerj\\Desktop\\csc_uploader\\CSC_Upload Call.txt
			System.out.println("Is File Exist: "+myFile.exists());
			inputStream = new FileInputStream(myFile); 
			
			System.out.println("INPUT STREAM"+inputStream.read());
		
			/*if (SSLAuthenticate.isValidateCertificate()) { */
				System.out.println("Input stream: "+inputStream.read());
				System.out.println("Start Date"+new Date());
				
				HashMap uploadResponse = CSCUploadHandler.doPost(inputParams, inputStream, authData);
				 System.out.println("upload response map : "+uploadResponse);
				
				System.out.println("End Date"+new Date());
			/*} else {
				System.out.print("SSL Authentication failed");
			}*/
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
