package com.cisco.brmspega.util.file;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateBasedLogUtil {

	private Pattern dateFormatPattern = Pattern.compile("<.*>");

	private Matcher matcher = null;

	private SimpleDateFormat sdf = null;

	private File logFile = null;

	private String logFilePreDateStr = null;

	private String logFileDateString = null;

	private String logFilePostDateStr = null;
	
	private String header = null;

	public DateBasedLogUtil(String logFileLoc, String header) throws Exception {
		this.header = header;
		matcher = dateFormatPattern.matcher(logFileLoc);
		if (matcher.find()) {
			String matchStr = matcher.group();
			String dateFormat = matchStr.substring(1, matchStr.length() - 1);
			sdf = new SimpleDateFormat(dateFormat);
			int matcherIndex = logFileLoc.indexOf(matchStr);
			logFilePreDateStr = logFileLoc.substring(0, matcherIndex);
			logFileDateString = sdf.format(new Date());
			logFilePostDateStr = logFileLoc.substring(matcherIndex + matchStr.length());
			logFile = new File(logFilePreDateStr + logFileDateString + logFilePostDateStr);
			if (!logFile.exists()) {
				FileUtil.appendToFile(logFile, header);
			}
		} else {
			throw new Exception("Date format missing in the File \"" + logFileLoc + "\"");
		}
	}

	public synchronized String logDetails(String fileLine) throws IOException {
		//String dateString = sdf.format(getDate());
		String dateString = sdf.format(new Date());
		if (!dateString.equals(logFileDateString)) {
			logFile = new File(logFilePreDateStr + dateString + logFilePostDateStr);
			if (!logFile.exists()) {
				FileUtil.appendToFile(logFile, header);
			}
		}
		FileUtil.appendToFile(logFile, fileLine);
		return "Written '" + fileLine + "' to file '" + logFile.getAbsolutePath() + "'.";
	}

	public static int count = 0;

	private Date getDate() {
		int quotient = count / 10;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, quotient);
		Date day = calendar.getTime();
		count++;
		return day;
	}

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 100; i++) {
			DateBasedLogUtil dateBasedLogUtil = new DateBasedLogUtil("C:/userserveractvity-<MMM-dd-yyyy>.log", "Datetime\tAction\tUsername\tCount");
			System.out.println(dateBasedLogUtil.logDetails(new Date() + "\tRestart\tskatare\t" + count));
		}
	}
	
}
