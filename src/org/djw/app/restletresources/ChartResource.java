package org.djw.app.restletresources;

import java.util.ArrayList;
import java.util.Collections;

import openfda.classes.OpenFDAClient;
import openfda.classes.ServerAuth;

import org.apache.log4j.Logger;
import org.djw.tools.json.ResponseJson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class ChartResource extends ServerResource {
	final static Logger logger = Logger.getLogger(ChartResource.class);
	/**
	 * Pulls result data from a search and formats for display in charts.  
	 * @param String ServerKey Used to authenticate the server requesting data
	 * @param String ResultType The type of search being performed. drugs or reactions
	 * @param String ThingList The list of items to search for
	 * @return JSONObject
	 * @throws JSONException

	 * 		
	 */
	@Get
	public Representation represent() throws JSONException {
		int StatusCode = 0;
		String Message = "";
		String ServerKey = "";
		if (getRequest().getAttributes().get("ServerKey") != null ) ServerKey = (String) getRequest().getAttributes().get("ServerKey");
		ServerAuth serverAuth = new ServerAuth();
		boolean Authenticated = serverAuth.authenticate(ServerKey);
		JSONObject jBody = new JSONObject();
		if (Authenticated){
			JSONArray chartdata = new JSONArray();
			String ResultType = "";
			if (getRequest().getAttributes().get("ResultType") != null ) ResultType = (String) getRequest().getAttributes().get("ResultType");
			String ThingList = "";
			if (getRequest().getAttributes().get("ThingList") != null ) ThingList = (String) getRequest().getAttributes().get("ThingList");
			
			String Things = "";
			if (!ResultType.equals("")){
				if (ResultType.equals("drugs")){
					String[] drugs = ThingList.split("~");
					String Drug = "";
					for (int r=0; r<drugs.length; r++){
						Drug += "patient.drug.openfda.substance_name:\"" + drugs[r] + "\"";
						if (r < drugs.length -1){
							Drug += "+AND+";
						}
					}
					Things = Drug;
				}
				if (ResultType.equals("reactions")){
					String[] reactions = ThingList.split("~");
					String Reaction = "";
					for (int r=0; r<reactions.length; r++){
						Reaction += "patient.reaction.reactionmeddrapt:\"" + reactions[r] + "\"";
						if (r < reactions.length -1){
							Reaction += "+AND+";
						}
					}
					Things = Reaction;
				}
			}
			
			String ServiceURI = "/event.json?search=" + Things + "&count=patient.patientonsetage";
	
			try{
				OpenFDAClient restClient = new OpenFDAClient();
				JSONObject json = restClient.getService(ServiceURI);
				JSONArray results = json.getJSONArray("results");
				
				JSONArray chartHeader = new JSONArray();
				chartHeader.put("Time");
				chartHeader.put("Count");
				chartdata.put(chartHeader);
				ArrayList<Integer> Terms = new ArrayList<Integer>();
				ArrayList<Integer> Counts = new ArrayList<Integer>();
				
				int numResults = results.length();
				for (int i=0; i<numResults; i++){
					JSONObject result = results.getJSONObject(i);
					String term = result.getString("term");
					int numDec = term.indexOf(".");
					if (numDec > -1 ){
						term = term.substring(0, term.indexOf("."));
					}
					int iTerm = Integer.parseInt(term);
					if (iTerm < 100){
						Terms.add(iTerm);
						Counts.add(result.getInt("count"));
					}
				}
				ArrayList<Integer> newOrder = new ArrayList<Integer>();
				for (int i=0; i<Terms.size(); i++){
					newOrder.add(Terms.get(i));
				}
				Collections.sort(newOrder);
				for (int i=0; i<newOrder.size(); i++){
					int idx = Terms.indexOf(newOrder.get(i));
					JSONArray jRec = new JSONArray();
					jRec.put(Terms.get(idx));
					jRec.put(Counts.get(idx));
					chartdata.put(jRec);
				}

				jBody.put("chartdata", chartdata);
			} catch (Exception e){
				logger.fatal("an error has occurred: " + e);
			}
			StatusCode = 0;
		} else {
			StatusCode = 1;
			Message = "Invalid authorization key.";
		}
		ResponseJson jResponse = new ResponseJson();
		Representation rep = null;
		jResponse.setStatusCode(StatusCode);
		jResponse.setStatusMessage(Message);
		jResponse.setBody(jBody);
		rep = new JsonRepresentation(jResponse.getResponse(jResponse));
		return rep;
	}
}
