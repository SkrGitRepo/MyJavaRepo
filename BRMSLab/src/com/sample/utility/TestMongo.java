package com.sample.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cisco.dataconnect.process.Load;
import com.cisco.dataconnect.utils.SingleInstance;
import com.cisco.ibpm.kafka.Eman;

public class TestMongo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 /*Load load = new Load();
		 //load.bulk();
		 //System.out.println("POSTED JSON::::"+jsonString);
		 System.out.println("value : " + load.search( "{  $or: [ {'_id' : 'RS-10525'} ] }"  )) ;*/
		 try {
			System.out.println("Dev ::" + ((Eman) SingleInstance.getInstance("DEV", "brms-nprd1-dev6:9092,brms-nprd1-dev7:9092,brms-nprd1-dev8:9092")).ping() );
			System.out.println("Stage ::" + ((Eman) SingleInstance.getInstance("STAGE", "brms-nprd1-stg1:9092,brms-nprd1-stg2:9092,brms-nprd1-stg3:9092")).ping() );
			System.out.println("PROD ::" + ((Eman) SingleInstance.getInstance("PROD", "brms-prd1-23:9092,brms-prd1-23:9092,brms-prd1-25:9092,brms-prd4-23:9092,brms-prd4-23:9092,brms-prd4-25:9092")).ping() );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
