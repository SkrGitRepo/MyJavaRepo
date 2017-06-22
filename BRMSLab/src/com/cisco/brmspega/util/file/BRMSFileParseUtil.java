package com.cisco.brmspega.util.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class BRMSFileParseUtil {
	
	
	public List<String> get_brms_db_cfg_report(String fileName) throws IOException {
		String dbCfgFileDir = "/opt/brms/install/";

		Collection<File> fileList = new ArrayList<File>();
		Iterator<File> fileListIterator = null;
		List<String> hostList = new ArrayList<String>();
		HashMap<String, Integer> uniqueDomainMap = new HashMap<String, Integer>();
		Map<String, Integer> sortedUniqueDomMap = null;
		String fileFilterString = null;

		//fileFilterString = "BRMS_ALL_ALL_DOMAINs_DB_CONF_REPORT.csv";
		fileFilterString = fileName;
		File dbCfgFile = new File(dbCfgFileDir + fileFilterString);
		if (dbCfgFile.exists()) {
			fileList = FileUtils.listFiles(new File(dbCfgFileDir), new WildcardFileFilter(fileFilterString), null);
			File file;
			fileListIterator = fileList.iterator();

			while (fileListIterator.hasNext()) {
				file = (File) fileListIterator.next();

				if (file.exists()) {
					List<String> lines = FileUtils.readLines(file);
					for (String line : lines) {
						if ((!line.contains("CONTEXT")) || (!line.contains(""))) {
							if (!line.contains("#")) {
								hostList.add(line);
							}
						}
					}
				}
			}

			/*if (!hostList.isEmpty()) {
				Collections.sort(hostList);
				int domain_count = 1;
				for (String data : hostList) {

					String[] tokens = data.split("\\,");
					String dom_name = tokens[1];
					if (!uniqueDomainMap.containsKey(dom_name)) {
						uniqueDomainMap.put(dom_name, domain_count);
						domain_count += 1;
					}
					// domain_count += 1;
				}
				sortedUniqueDomMap = new TreeMap<String, Integer>(uniqueDomainMap);
			} else {
				System.out.println(" hostlist is empty now");
			}*/

		} else {
			hostList = null;
		}
		return hostList;
	}
	
	public void brms_vm_cfg_file_reader() {
		
	}
}
