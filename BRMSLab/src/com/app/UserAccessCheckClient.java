package com.app;

import com.cisco.brmspega.useracess.UserUtil;
import com.cisco.brmspega.util.EnvBiListMapCache;

public class UserAccessCheckClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Processing user role validation check .. Pleasse wait ...");
		String userId = "bhbalasu";//"jabanerj";//"ankurku2";//"vgudalor";//"dgarapat";//UserUtil.getUserId(request);
		//boolean isSysAdmin = UserUtil.isUserValid(userId, "BRMS_ADMIN") || UserUtil.isUserValid(userId, "BRM_ADMIN");
		boolean isSysAdmin =  UserUtil.isUserValid(userId, "BRMS_ADMIN");
		boolean isWLSysAdmin = UserUtil.isUserValid(userId, "BRMS_WL_ADMIN");
		
		
		boolean tempAdminUser = userId.matches("sumkuma2|dursingh"); 
		 
		
		String env = "dev";
		String inputDomain =  "/csc/sa";
		String domain_name = inputDomain.split("\\/")[1];
		String subString = inputDomain.substring(1);//.indexOf("/");
		String tmpUsrID = "abhijban";
		
		System.out.println("Domain name :"+ domain_name + "domainSubstring" + subString);
		
		
		boolean isUIServerAdmin =  UserUtil.isUserValid(userId,UserUtil.getRegEx("BRMS",EnvBiListMapCache.getInstance().getLdapEnvs(env,"|","(",")"),"ea",null,"UI_ADMIN"));
		
		boolean isServerAdmin =  UserUtil.isUserValid(userId,UserUtil.getRegEx("BRMS",EnvBiListMapCache.getInstance().getLdapEnvs(env,"|","(",")"),"ea",null,"ADMIN"));
		boolean isDomainServerAdmin =  UserUtil.isUserValid(tmpUsrID,UserUtil.getRegEx("BRMS",EnvBiListMapCache.getInstance().getLdapEnvs(env,"|","(",")"),"sre",null,"SERVER_ADMIN"));
		//boolean isServerAdmin =  UserUtil.isUserValid(userId,UserUtil.getRegEx("BRMS",null,null,null,"ADMIN"));

		//UserUtil.isUserValid(userId,UserUtil.getRegEx("BRMS",EnvBiListMapCache.getInstance().getLdapEnvs(fileLineData[10],"|","(",")"),
//fileLineData[9].substring(1,(fileLineData[9].substring(1).indexOf("/") != -1 ? fileLineData[9].substring(1).indexOf("/") + 1: fileLineData[9].length())),null,"ADMIN")): false)
//|| ((fileLineData.length > 9 && !"".equals(fileLineData[9])) ?
		
		if(isServerAdmin) {
			System.out.println(" USER :"+userId + " is Server Admin");
		}  else {
			System.out.println(" USER :"+userId + " is not Server Admin");
		}
		
		if(tempAdminUser) {
			System.out.println(" USER :"+userId + " is part of temp Server Admin");
		}  else {
			System.out.println(" USER :"+userId + " is not part of temp Server Admin");
		}
		
		
		if(isUIServerAdmin) {
			System.out.println(" USER :"+userId + " is BRMS UI Server Admin");
		}  else {
			System.out.println(" USER :"+userId + " is not a BRMS UI Server Admin");
		}
		
		if (isSysAdmin) {
			System.out.println(" USER :"+userId + " is System Admin");
		} else {
			System.out.println(" USER :"+userId + " is not System Admin");
		}
		
		if (isWLSysAdmin) {
			System.out.println(" USER :"+userId + " is WL System Admin");
		} else {
			System.out.println(" USER :"+userId + " is not WL System Admin");
		}
		
		
		if (isDomainServerAdmin) {
			System.out.println(" USER :"+tmpUsrID + " is DOmain  System Admin");
		} else {
			System.out.println(" USER :"+tmpUsrID + " is not a Domain System Admin");
		}
	}

}
