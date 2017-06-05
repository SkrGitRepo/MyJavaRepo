package com.cisco.brmspega.ws;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.print.attribute.standard.Media;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sample.utility.ManageServerActionRequest;

@Path("/")
public class ServerActionRESTService {
	@POST
	@Path("/serverActionService")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response serverActionREST(InputStream incomingData) {
		StringBuilder serverActionBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				serverActionBuilder.append(line);
			}
			if(serverActionBuilder != null) {
				System.out.println("Server Action Request Received: " + serverActionBuilder.toString());
				ManageServerActionRequest sa = new ManageServerActionRequest();
				sa.manageServerActionRequest(serverActionBuilder.toString());
			}
			
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(serverActionBuilder.toString()).build();
	}
	
/*	@POST
	@Path("/serverAction")
	@Produces(MediaType.TEXT_PLAIN)
	public Response serverAction(@FormParam("env") String env,@FormParam("domain") String domain,@FormParam("jvm") String jvm) {
		System.out.println(env);
        return Response.ok("Env=" + env +" Domain= "+domain+" JVM= "+jvm).build();
		// return HTTP response 200 in case of success
		//return Response.status(200).entity(serverActionBuilder.toString()).build();
	}
*/
}
