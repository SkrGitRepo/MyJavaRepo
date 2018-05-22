package com.cisco.brmspega.util.file;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class GernarateCertReport {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		 try {
		        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		        keystore.load(new FileInputStream("/Users/sumkuma2/Desktop/brmsclient.jks"), "password".toCharArray());
		        Enumeration<String> aliases = keystore.aliases();
		        while(aliases.hasMoreElements()){
		            String alias = aliases.nextElement();
		            if(keystore.getCertificate(alias).getType().equals("X.509")){
		            	System.out.println(alias + " Subject DSN: " + ((X509Certificate) keystore.getCertificate(alias)).getSubjectDN());
		            	System.out.println(alias + " DSN: " + ((X509Certificate) keystore.getCertificate(alias)).getIssuerDN());
		            	System.out.println(alias + " Valid From: " + ((X509Certificate) keystore.getCertificate(alias)).getNotBefore());
		                System.out.println(alias + " Expires On: " + ((X509Certificate) keystore.getCertificate(alias)).getNotAfter());
		                System.out.println(alias + " Provider: " +  keystore.getProvider());
		                System.out.println(alias + " CreationDate: " +  keystore.getCreationDate(alias));
		                System.out.println(alias + " CERTIFICATE: " +  keystore.getCertificate(alias));
		                //( (X50Certificate) keystore.getCertificate(alias)).getNotBefore());
		            }
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		
	}

}
