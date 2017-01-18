/**
 * 
 */
package com.cisco.dataconnect.utils;

import java.net.InetAddress;

/**
 * @author santkris
 *
 */
public class ping {

	/**
	 * 
	 */
	public ping() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ping p = new ping(); 
		System.out.println("Value" + p.test() ); 

	}
	
	public boolean test ()
	{
	        try{
	            InetAddress address = InetAddress.getByName("192.168.1.10388");
	            return   address.isReachable(1000);
	        } catch (Exception e)
	        {
	            e.printStackTrace();
	            return false;
	        }
	   
	}

}
