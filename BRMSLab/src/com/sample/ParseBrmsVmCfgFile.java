package com.sample;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;


public class ParseBrmsVmCfgFile {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
		Collection<File> fileList = FileUtils.listFiles(new File("/opt/brms/shared/scripts/"), new String[] { "txt" }, false);
		Iterator<File> fileListIterator = fileList.iterator();
		String strTempLifeCycle = "DEV";
	
		
		
		while (fileListIterator.hasNext()) {
			//System.out.println("AM I HERE");
			File file = (File) fileListIterator.next();
			//System.out.println(file.getAbsolutePath());
			System.out.println(file.getName());
			//if(file.getName().matches("(?i:.brms_vm_cfg_*_" + strTempLifeCycle +".txt)") && file.getName().indexOf("_ADM") == -1){
			if(file.getName().equalsIgnoreCase("brms_vm_cfg_NPRD2_DEV.txt")){
				System.out.println(file.getAbsolutePath());
				List<String> fileLinesList = FileUtils.readLines(file);
				for(String strFileLine : fileLinesList){
					if (!strFileLine.startsWith("#")) {
						if(!strFileLine.isEmpty()) {
							String[] strArrFileLineData = strFileLine.split(",", -1);
							System.out.println("Domain:"+strArrFileLineData[9]);
							String strArrDomainApp[] = strArrFileLineData[9].split("/");
							//System.out.println("*** UPDATED HOST PARAM"+strArrFileLineData[9]);
							String[] jvm_name = strArrFileLineData[1].split("/")[5].split("-");
							String exsting_jvm_port = null;
							exsting_jvm_port = jvm_name[jvm_name.length - 1];
						}
					
					}
				}
			}
		}
	}

}
