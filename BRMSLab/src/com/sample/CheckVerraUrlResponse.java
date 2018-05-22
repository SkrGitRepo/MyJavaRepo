package com.sample;

public class CheckVerraUrlResponse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			  
			  String tenant = "ciscostage";
			        String serverUrl = "https://ciscostage.vera.com";
			        String keyServiceUrl = "https://vera-stg.cisco.com/kc";
			        String appId = "d228d4d1-c3da-3532-ab02-e9156b1c19c9";
			        String appSecret = "Qm16dDA5UmVDd2g2UFFCQUpMTG54QW9sd3lzPXxkMjI4ZDRkMS1jM2RhLTM1MzItYWIwMi1lOTE1NmIxYzE5Yzl8OWViNWVkYzgtMTdmNi00YjVjLWIwNjMtOTMyZDQ2NDczZjE0fDE1MDQ4NzY3ODA2MzB8REVWSUNFfFJPTEVfREVWSUNFfDI2ODQzNTQ1NnwxNTA0ODc2NzgwNjMwfGZhbHNlfG51bGx8ZWtqVEx0NmhQVm0zdkxBNllZWnQ3eWtXbjlmT0g2OW5vNmF1aFVkcksvbHAwZUdKeU5BQi9TZHc2KzVSSmtLZDlZUXp5Q0o1Nkx1anlMRHVtRHVUV0M4RGYyRVdIakpFbFYwVlhFVzQvM1BJSWV6ODJITUFETW5nbkZyc0J2K0duWFdqUmozeHFZS2JEZ1oxRmVLUUZ4TnIzVnZPSktYaS9oMExFd2UyWkxnPQ";
			  
			  
			        com.vera.sdk.Sdk.initialize();

			        com.vera.sdk.Context context = new com.vera.sdk.Context();

			        context.setTenant(tenant);
			        context.setServerUrl(serverUrl);
			        context.setKeyServiceUrl(keyServiceUrl);
			        context.setAppId(appId);
			        context.setAppSecret(appSecret);
			        
			        com.vera.sdk.Securer securer = new com.vera.sdk.Securer(context);
			        securer.revokeAllAccessForDocId("84373c5e-1fe3-31a1-872f-5f11d36e62a7");
			        System.out.println("Completed");
			  
			  
			}

			catch (Exception e){

			       e.printStackTrace();
			       //firstEmail = "jabanerj";
			       
			    
			  }

	}

}
