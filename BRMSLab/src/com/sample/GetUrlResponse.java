package com.sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetUrlResponse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String resMessage = null;
		String webPage = "https://ibpmadm-stage.cisco.com/ea/PRRestService/CiscoSample/Services/eman";
		try {
			//System.out.println("INPUT EMAN URL TO CHECK::"+webPage);
			URL url = new URL(webPage);

			HttpURLConnection httpCon = (HttpURLConnection)url.openConnection();
			//httpCon.setRequestProperty("Authorization", "Basic " + authStringEnc);
			int statusCode = httpCon.getResponseCode();
			InputStream is = httpCon.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			//String resContent  =  http.getContentEncoding().toString();
			
			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			String emanResponse = sb.toString();
			
			//System.out.println(emanResponse);
			if (emanResponse.contains("EMAN OK")) {
				System.out.println(emanResponse);
				System.out.println("SUCCESS EMAN Response: "+emanResponse);
				resMessage = "Up";
			} else {
				System.out.println("Failure EMAN Response"+emanResponse);
				resMessage = "Down";
			}
			//System.out.println(" *******************************");
			
		} catch (MalformedURLException e) {
			//e.printStackTrace();
			//System.out.println("** Exception :: Malformed URL Exception");
		} catch (IOException e) {
			//e.printStackTrace();
			//System.out.println("** Exception :: IO Exception");
		} finally {
			if (resMessage == null) {
				resMessage = "Down";
			}
		}
	}

}
