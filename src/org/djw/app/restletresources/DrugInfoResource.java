package org.djw.app.restletresources;


import openfda.classes.DailyMedClient;
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

public class DrugInfoResource extends ServerResource {
	final static Logger logger = Logger.getLogger(DrugInfoResource.class);
	
	/**
	 * Pulls information on drugs from open.fda.gov and enhances the result with additional data from DailyMed.  
	 * @param String ServerKey Used to authenticate the server requesting data
	 * @param String DrugName Name of drug needing information
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
			try{
				String DrugName = "";
				String spl_set_id = "";
				JSONObject DrugInfo = new JSONObject();
				if (getRequest().getAttributes().get("DrugName") != null ) DrugName = (String) getRequest().getAttributes().get("DrugName");
		
				String ServiceURI = "/label.json?search=openfda.substance_name:\"" + DrugName + "\"";
	
				OpenFDAClient restClient = new OpenFDAClient();
				JSONObject json = restClient.getService(ServiceURI);
				if (!json.isNull("results")){
					JSONArray results = json.getJSONArray("results");
					if (results.length() > 0){
						JSONObject drug = results.getJSONObject(0);
						JSONObject openfda = drug.getJSONObject("openfda");
						
						spl_set_id = openfda.getJSONArray("spl_set_id").getString(0);
						DrugInfo.put("brand_name", openfda.getString("brand_name"));
						DrugInfo.put("generic_name", openfda.getString("generic_name"));
						DrugInfo.put("manufacturer_name", openfda.getString("manufacturer_name"));
						DrugInfo.put("product_ndc", openfda.getString("product_ndc"));
						DrugInfo.put("product_type", openfda.getString("product_type"));
						DrugInfo.put("route", openfda.getString("route"));
						DrugInfo.put("spl_set_id", spl_set_id);
						DrugInfo.put("substance_name", openfda.getString("substance_name"));
					}
					
					ServiceURI = "/services/v2/spls/" + spl_set_id + "/media.json";
					DailyMedClient medClient = new DailyMedClient();
					JSONObject jsonMed = medClient.getService(ServiceURI);
					JSONObject data = jsonMed.getJSONObject("data");
					JSONArray media = data.getJSONArray("media");
					
					JSONArray images = new JSONArray();
					for (int i=0; i<media.length(); i++){
						JSONObject rec = media.getJSONObject(i);
						if (rec.getString("mime_type").equals("image/jpeg")){
							String ImageURL = rec.getString("url");
							images.put(ImageURL);
						}
					}
					
									
					Message = "";
					Status = 0;
					DrugInfo.put("images", images);
					jBody.put("DrugInfo", DrugInfo);
				} else {
					jBody.put("DrugInfo", DrugInfo);
				}
			} catch (Exception e){
				Status = 1;
				Message = "an error occurred: " + e;
			}
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

