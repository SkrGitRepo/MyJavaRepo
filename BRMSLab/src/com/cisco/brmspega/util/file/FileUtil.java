package com.cisco.brmspega.util.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

	public static void appendToFile(File appendFile, String content) throws IOException {
		FileWriter fstream = null;
		BufferedWriter out = null;
		try {
			fstream = new FileWriter(appendFile, true);
			out = new BufferedWriter(fstream);
			out.write("\n");
			out.write(content);
		} finally {
			out.close();
			fstream.close();
		}
	}

}
