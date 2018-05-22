package com.cisco.brmspega.cache;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.cisco.brmspega.bundles.PropertyLoader;

public class DomainCache {

	private static Map<String, String> domainDescMap = null;

	private static DomainCache domainCache = null;

	private DomainCache() {

	}

	static {
		try {
			domainCache = new DomainCache();
			String folderLoc = PropertyLoader.getInstance().getProperty("server_tools_cofig_base");
			File file = new File(folderLoc + "/domain.cfg");
			@SuppressWarnings("unchecked")
			List<String> fileLines = FileUtils.readLines(file);
			int commaIndex;
			domainDescMap = new HashMap<String, String>();
			for (String fileLine : fileLines) {
				commaIndex = fileLine.indexOf(",");
				domainDescMap.put(fileLine.substring(0, commaIndex).trim().toUpperCase(), fileLine.substring(commaIndex + 1, fileLine.length()).trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getDomainDesc(String domain) {
		return domainDescMap.get(domain.toUpperCase());
	}

	public static DomainCache getInstance() {
		return domainCache;
	}
	
	/*public static void main(String[] args) {
		DomainCache domainCache = DomainCache.getInstance();
		for (String domain : domainCache.domainDescMap.keySet()) {
			System.out.println("Domain : " + domain + "\tDesc : " + domainCache.domainDescMap.get(domain));
		}
	}*/

}
