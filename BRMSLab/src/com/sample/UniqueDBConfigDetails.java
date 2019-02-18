package com.sample;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.cisco.brmspega.dvo.DataSourceDVO;
import com.cisco.brmspega.dvo.TcDataSourceDVO;

public class UniqueDBConfigDetails {

	private static final String m_scriptsFolderLoc = "/opt/brms/install/config";
	private static final String m_appAndDbFolderLoc = "/opt/brms/install/tc_install/config";
	//private static final String m_appAndDbFolderLoc = "/opt/brms/install/config";
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stu
		String strDomainName =  "cpe";
		String strLifeCycle =  "PRD";
		String hostList = "brms-prd1-01:8101,brms-prd1-02:8101,brms-prd1-13:8100,brms-prd1-12:8100";
		String[] strUpdatehostParams = hostList.split(":");
		Set<DataSourceDVO> dsConfSet = fetchDSConfigForSelectedHost(strDomainName, strUpdatehostParams, strLifeCycle);
		
		System.out.println("DS SIZE:"+dsConfSet.size());
		
		for(DataSourceDVO dsDVO : dsConfSet) 
		{	
			
			System.out.println("DS Name: "+dsDVO.getM_strDataSourceName());
			
		}
		

	}

	private static Set<DataSourceDVO> fetchDSConfigForSelectedHost(String strDomainName, String[] strUpdatehostParams, String strLifeCycle) throws IOException{	
		Set<DataSourceDVO> dsInfoSet = new LinkedHashSet<DataSourceDVO>();
		Collection<File> fileList = FileUtils.listFiles(new File(m_appAndDbFolderLoc), new String[] { "txt" }, false);
		Iterator<File> fileListIterator = fileList.iterator();		
		String strTempLifeCycle = getLifeCycleToCompareFileName(strLifeCycle);
		while (fileListIterator.hasNext()) {
			File file = (File) fileListIterator.next();
//			fileName.matches("(?i:.*_db_cfg_.*_DEV)")
//			text.matches(pattern);
			if(file.getName().matches("(?i:.*_db_cfg_.*_" + strTempLifeCycle +".txt)") && file.getName().indexOf("_ADM") == -1){
				List<String> fileLinesList = FileUtils.readLines(file);
				
				for(String strFileLine : fileLinesList){
					if (!strFileLine.startsWith("#")) {
						String[] strArrFileLineData = strFileLine.split(",", -1);
						System.out.println("***FIRST "+ strUpdatehostParams[0] + ":" + strArrFileLineData[0]);
						System.out.println("** SECOND "+ strUpdatehostParams[2] + ":" + strArrFileLineData[1]);
						if (strDomainName.equalsIgnoreCase(strArrFileLineData[2]) 
								&&( null == strUpdatehostParams || 
									( strUpdatehostParams[0].equals(strArrFileLineData[0])
										&& strUpdatehostParams[2].equals(strArrFileLineData[1]) 
									)
							  ) ) {
							DataSourceDVO dsDVO = new DataSourceDVO();
							dsDVO.setM_strDataSourceName(strArrFileLineData[3]);
							dsDVO.setM_strJndiName(strArrFileLineData[4]);
							dsDVO.setM_strConnString(strArrFileLineData[5]);
							dsDVO.setM_strUserName(strArrFileLineData[6]);
							if(strArrFileLineData.length > 12){ 
								dsDVO.setM_strPassword(strArrFileLineData[7]);
								dsDVO.setM_strTimeOut(strArrFileLineData[8]);
								dsDVO.setM_strMinConn(strArrFileLineData[9]);
								dsDVO.setM_strMaxConn(strArrFileLineData[10]);
								dsDVO.setM_strConnRetryFreq(strArrFileLineData[11]);
								dsDVO.setM_strConnResumeTimeout(strArrFileLineData[12]);
								dsDVO.setM_strConnInactTimeout(strArrFileLineData[13]);
								dsDVO.setM_strLoginDelay(strArrFileLineData[14]);
								dsDVO.setM_strStmtCache(strArrFileLineData[15]);
							} else {
								dsDVO.setM_strPassword("");
								dsDVO.setM_strTimeOut("");
								dsDVO.setM_strMinConn("");
								dsDVO.setM_strMaxConn("");
								dsDVO.setM_strConnRetryFreq("");
								dsDVO.setM_strConnResumeTimeout("");
								dsDVO.setM_strConnInactTimeout("");
								dsDVO.setM_strLoginDelay("");
								dsDVO.setM_strStmtCache("");
							}
							dsInfoSet.add(dsDVO);
						}
					}
				}
			}
		}
		
		return dsInfoSet;
	}
	
	private static String getLifeCycleToCompareFileName(String strLifeCycle){
		return "PROD".equalsIgnoreCase(strLifeCycle) ? "PRD" :
			   "STAGE".equalsIgnoreCase(strLifeCycle) ? "STG" :
			    strLifeCycle;
	}
}
