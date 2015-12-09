package org.djw.app.restletresources;


import openfda.classes.ServerAuth;

import org.apache.log4j.Logger;
import org.djw.tools.db.DBTableJNDI;
import org.djw.tools.json.OutputFormatter;
import org.djw.tools.json.ResponseJson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class LookupResource extends ServerResource {
	final static Logger logger = Logger.getLogger(LookupResource.class);
	
	/**
	 * Returns lookup data for use in dropdown and autocomplete application controls.   
	 * @param String ServerKey Used to authenticate the server requesting data.
	 * @param String lookuptype Type of lookup to be performed. drugs or adverse reactions.
	 * @param String partial Partial string of drug or term to be looked up.
	 * @return JSONObject
	 * @throws JSONException
	 * 		
	 */
	@Get
	public Representation represent() throws JSONException {

		String Message = "";
		int Status = 0;
		JSONObject jBody = new JSONObject();
		
		String ServerKey = "";
		if (getRequest().getAttributes().get("ServerKey") != null ) ServerKey = (String) getRequest().getAttributes().get("ServerKey");
		ServerAuth serverAuth = new ServerAuth();
		boolean Authenticated = serverAuth.authenticate(ServerKey);

		if (Authenticated){
			String LookupType = "";
			if (getRequest().getAttributes().get("lookuptype") != null ) LookupType = (String) getRequest().getAttributes().get("lookuptype");
			String Partial = "";
			if (getRequest().getAttributes().get("partial") != null ) Partial = (String) getRequest().getAttributes().get("partial");
	
			
			String dbType = "";
			String sqlStatement = "";
			
			Partial = Partial.replace("%20"," "); //unencode space character
			if (LookupType.equals("reactions")){
				sqlStatement = "select reaction from reactions ";
				if (!Partial.equals("")){
					sqlStatement += "where reaction like '%" + Partial + "%' ";
				}
				sqlStatement += "order by reaction";
			}
			if (LookupType.equals("drugs")){
				sqlStatement = "select drugname from drugs ";
				if (!Partial.equals("")){
					sqlStatement += "where drugname like '%" + Partial + "%' ";
				}
				sqlStatement += "order by drugname";
			}
	
				if (logger.isDebugEnabled()){
					logger.debug("sqlStatement: " + sqlStatement);
				}

			DBTableJNDI dbTable = new DBTableJNDI();
			dbTable = dbTable.getDBTableResults(sqlStatement, dbType);
			OutputFormatter formatter = new OutputFormatter();
			JSONArray jResults = formatter.getJsonArray(dbTable);
			
			Message = "";
			Status = 0;
			jBody.put("results", jResults);
		} else {
			Status = 1;
			Message = "invalid authentication token";
		}
		ResponseJson jResponse = new ResponseJson();
		Representation rep = null;
		jResponse.setStatusCode(Status);
		jResponse.setStatusMessage(Message);
		jResponse.setBody(jBody);
		rep = new JsonRepresentation(jResponse.getResponse(jResponse));
		return rep;
	}
}

