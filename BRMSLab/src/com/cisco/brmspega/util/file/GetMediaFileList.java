package com.cisco.brmspega.util.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class GetMediaFileList {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String mediaHomeDir = "/Users/sumapr/git/MyJavaRepo/BRMSLab/WebContent/audio";
		//Collection<File> mp3FileList = new ArrayList<File>();
		//Iterator<File> mp3FileListIterator = null;
		//String fileFilterString = "*.mp3";
		//File mp3Files = new File(mediaHomeDir + fileFilterString);
		//mp3FileList = FileUtils.listFiles(new File(mediaHomeDir), new WildcardFileFilter(fileFilterString), null);
		
		GetMediaFileList mediaObj = new GetMediaFileList();
		List<String> mp3AudioList = mediaObj.getMP3FileList(mediaHomeDir);
		
		
		for (String mp3FileName : mp3AudioList) {
			
			System.out.println("File Name: "+mp3FileName);
		}
		
		
	}
	
	public List<String> getMP3FileList(String mediaHomeDir) {
		
		Collection<File> mp3FileList = new ArrayList<File>();
		Iterator<File> mp3FileListIterator = null;
		String fileFilterString = "*.mp3";
		mp3FileList = FileUtils.listFiles(new File(mediaHomeDir), new WildcardFileFilter(fileFilterString), null);
		
		File file;
		mp3FileListIterator = mp3FileList.iterator();
		List<String> mp3AudioList = new ArrayList<String>();

		while (mp3FileListIterator.hasNext()) {
			file = (File) mp3FileListIterator.next();
			if (file.exists()) {
				String mp3FileName = file.getName();
				String mp3FilePath = file.getAbsolutePath();
				System.out.println(" File :"+mp3FilePath);
				mp3AudioList.add(mp3FileName);
			}
		}
		
		return mp3AudioList;
	}

}
