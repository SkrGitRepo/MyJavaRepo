package com.cisco.brmspega.util.ts;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author skatare
 */
public class TimeStampCreator {

	private static Random rn = new Random(1234567890);

	/**
	 * @param filepath
	 * @return
	 */
	public static String createTimeStamp() {
		synchronized (TimeStampCreator.class) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Date d = new Date();
			return sdf.format(d) + rn.nextInt();
		}
	}
}
