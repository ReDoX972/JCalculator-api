package com.blueteam.rest;
 
 
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.JSONException;
import org.json.JSONObject;
 
@Path("/math")
public class CalcService {

	ScriptEngine engine;
	
	public CalcService() {
		// Create Javascript parser
		ScriptEngineManager mgr = new ScriptEngineManager();
		this.engine = mgr.getEngineByName("JavaScript");
	}
	
	@GET
	public Response EvaluateExpression(@QueryParam("expression") String s) throws JSONException {
		
		JSONObject json = new JSONObject();
		ResponseBuilder resp;
		
		try {
			// Try to parse the mathematical expression parameter and put it into json object
			json.put("result", engine.eval(s)); 
			
			// Build the OK response
			resp = Response.ok(json.toString(), MediaType.APPLICATION_JSON);
			
		} catch (ScriptException e) {
			
			resp = Response
					.status(Response.Status.BAD_REQUEST)
					.entity("syntax error")
					.type(MediaType.TEXT_PLAIN);
		}
		
		return resp.build();
	}	
}