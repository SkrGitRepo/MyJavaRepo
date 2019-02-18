package com.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.io.IOException;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import java.security.spec.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Collection;

public class ImportCertInKStore {

	public static void main(String[] argv) throws Exception {

		String certfile = "/opt/brms/shared/certs/to_install/QuoVadisRootCA2.crt"; /*your cert path*/
		FileInputStream is = new FileInputStream("/opt/brms/shared/certs/brmsclient.jks");

		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(is, "password".toCharArray());

		String alias = "tools_cisco_com";
		char[] password = "password".toCharArray();

		//////

		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		InputStream certstream = fullStream (certfile);
		Certificate certs =  cf.generateCertificate(certstream);

		///
		File keystoreFile = new File("/opt/brms/shared/certs/brmsclient.jks");
		// Load the keystore contents
		FileInputStream in = new FileInputStream(keystoreFile);
		keystore.load(in, password);
		in.close();

		// Add the certificate
		keystore.setCertificateEntry(alias, certs);

		// Save the new keystore contents
		FileOutputStream out = new FileOutputStream(keystoreFile);
		keystore.store(out, password);
		out.close();

		}

		private static InputStream fullStream ( String fname ) throws IOException {
		    FileInputStream fis = new FileInputStream(fname);
		    DataInputStream dis = new DataInputStream(fis);
		    byte[] bytes = new byte[dis.available()];
		    dis.readFully(bytes);
		    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		    return bais;
		}

}