package com.sample.utility;

import com.cisco.dataconnect.process.Load;

public class TestMongo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 Load load = new Load();
		 //load.bulk();
		 //System.out.println("POSTED JSON::::"+jsonString);
		 System.out.println("value : " + load.search( "{  $or: [ {'_id' : 'RS-10525'} ] }"  )) ;

	}

}
