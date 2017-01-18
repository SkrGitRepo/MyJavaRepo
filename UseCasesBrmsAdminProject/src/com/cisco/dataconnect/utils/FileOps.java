package com.cisco.dataconnect.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;


class FileOps {

	public FileOps() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public Iterator<File> getFiles(String dirPath, String fileType )
	{
		@SuppressWarnings("unchecked")
		Collection<File> excelFileList = FileUtils.listFiles(new File(dirPath), new String[] { fileType  }, false);
		return (  excelFileList.iterator() );
		
		/*
		File file = null;
		while (excelFileListIterator.hasNext()) {
			file = (File) excelFileListIterator.next();
		}
		return null;	
		*/
	}

}
