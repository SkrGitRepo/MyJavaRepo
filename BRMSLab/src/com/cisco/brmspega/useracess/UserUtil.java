package com.cisco.brmspega.useracess;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cisco.brmspega.bundles.PropertyLoader;
import com.cisco.brmspega.util.ldap.LdapCache;
import com.cisco.brmspega.util.ldap.LdapData;

public class UserUtil {
	
	public static String getUserId(HttpServletRequest req) {
		String userId = null;
		String localServer = PropertyLoader.getInstance().getProperty("LOCAL_SERVER");
		if (req != null) {
			userId = (String) req.getRemoteUser();			
			if(null == userId){
				userId = (String) req.getHeader("AUTH_USER");				
			}
		}
		if (userId == null) {	
			System.out.println(" LOCALSERVER from PROPERTY FILE:"+localServer);
			if (req.getServerName().equalsIgnoreCase("brms-test-5")) {
				userId="sumkuma2";
			} else if (req.getServerName().equalsIgnoreCase("brms-test-1")) {
				userId="sumkuma2";
			} else if (req.getServerName().equalsIgnoreCase("brms-test-4")) {
				userId="sumkuma2";
			} else if (req.getServerName().equalsIgnoreCase("brms-prd3-adm1")) {
				userId="sumkuma2";
			} else if( localServer != null && req.getServerName().equalsIgnoreCase(localServer)) {
				userId="sumkuma2";
			} else {
				userId = "guest";
			}
		}
		return userId;
	}
 
	public static boolean isUserValid(String userId, String regEx) {
		if (regEx != null) {
			// making it case insensitive
			regEx = "(?i)" + regEx;
		}
		boolean userValid = false;
		if("Y".equals(PropertyLoader.getInstance().getProperty("ldap_check_required"))){
			LdapData ldapData = LdapCache.getInstance().getLdapData(userId);
			if (ldapData != null) {
				List<String> userGroups = ldapData.getLdapGroups();
				//System.out.println("RegEx : " + regEx + ", UserGroups : " + userGroups.toString());
				Iterator<String> userGroupsIterator = userGroups.iterator();
				String userGroup = null;
				while (userGroupsIterator.hasNext() && !userValid) {
					userGroup = ((String) userGroupsIterator.next());
					if (null != userGroup && userGroup.matches(regEx)) {
						userValid = true;
						break;
					}
				}
			}
		}
		System.out.println("isUserValid ==> Logged in user : "+userId+" regEx : "+regEx+" userValid : "+userValid);
		//return "sumkuma2".equalsIgnoreCase(userId) ? true : userValid;
		return userValid;
	}

	public static String getRegEx(String platform, String lifeCycle, String domain, String app, String role) {
		String regEx = addFirst(platform);
		regEx += addIntermediate(regEx, lifeCycle);
		// TODO Fix this in the next release i.e. Q1 2014 
		if (domain != null && domain.indexOf("-") != -1) {
			domain = domain.substring(0, domain.indexOf("-"));
		}
		
		if (role.equalsIgnoreCase("server_admin")) {
			domain = domain+"_"+"SERVER";
			role = "ADMIN";
		}
			
		regEx += addIntermediate(regEx, domain);
		//regEx += addOptinalIntermediate(regEx, app);
		if (!"Admin".equalsIgnoreCase(role)) {
			regEx += addIntermediate(regEx, app);
		}
		regEx += addLast(regEx, role);
		System.out.println("getRegEx : platform : "+platform+" lifeCycle : "+lifeCycle+" domain : "+domain+" app :"+app+" role : "+role+" regEx : "+regEx);
		return regEx; 
	}

	private static String addFirst(String appender) {
		return (appender != null ? appender + "_" : ".*");
	}

	private static String addIntermediate(String regEx, String appender) {
		return (appender != null ? (regEx.endsWith(".*") ? "_" + appender + "_" : appender + "_") : (regEx.endsWith(".*") ? "" : ".*"));
	}
	
	/*private static String addOptinalIntermediate(String regEx, String appender) {
		return (appender != null ? (regEx.endsWith(".*") ? "_" + appender + "_" : appender + "_") : (regEx.endsWith(".*") ? "" : "(|.*_)"));
	}*/

	private static String addLast(String regEx, String appender) {
		return (appender != null ? (regEx.endsWith(".*") ? "_" + appender + ".*" : appender + ".*") : (regEx.endsWith(".*") ? "" : ".*"));
	}

	public static void main(String[] args) {
		System.out.println(UserUtil.getRegEx(null, null, null, null, null));
		System.out.println(UserUtil.getRegEx("BRMS", null, null, null, null));
		System.out.println(UserUtil.getRegEx(null, "dev", null, null, null));
		System.out.println(UserUtil.getRegEx(null, null, "gssc", null, null));
		System.out.println(UserUtil.getRegEx(null, null, null, "rma", null));
		System.out.println(UserUtil.getRegEx(null, null, null, null, "developer"));
		
		System.out.println(UserUtil.getRegEx("BRMS", "dev", null, null, null));
		System.out.println(UserUtil.getRegEx("BRMS", null, "gssc", null, null));
		System.out.println(UserUtil.getRegEx("BRMS", null, null, "rma", null));
		System.out.println(UserUtil.getRegEx("BRMS", null, null, null, "developer"));
		
		System.out.println(UserUtil.getRegEx("BRMS", "dev", null, null, null));
		System.out.println(UserUtil.getRegEx("BRMS", "dev", "gssc", null, null));
		System.out.println(UserUtil.getRegEx("BRMS", "dev", null, "rma", null));
		System.out.println(UserUtil.getRegEx("BRMS", "dev", null, null, "developer"));
		
		System.out.println(UserUtil.getRegEx("BRMS", null, null, "rma", "developer"));
		
		System.out.println(UserUtil.getRegEx("BRMS", null, "gssc", "rma", null));
		System.out.println(UserUtil.getRegEx("BRMS", null, "gssc", "rma", "admin"));
		System.out.println(UserUtil.getRegEx("BRMS", null, "gssc", "rma", "developer"));
		
	}
	}
