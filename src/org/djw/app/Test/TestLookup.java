package org.djw.app.Test;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestLookup {
	
	public TestLookup(){}
	
	public boolean testDrugPartial(String ServiceURL, String MatchString){
		boolean Success = false;
	
		ServiceClient serviceClient = new ServiceClient();
		try{
			String TestOutput = serviceClient.getService(ServiceURL);
			try{
				int numMatches = 0;
				JSONObject json = new JSONObject(TestOutput);
				JSONObject Body = json.getJSONObject("Body");
				JSONArray results = Body.getJSONArray("results");
				for (int i=0; i<results.length(); i++){
					String resultString = results.getString(i);
					if (MatchString.equals(resultString)){
						numMatches++;
					}
				}
				if (numMatches > 0){
					Success = true;
				}
			} catch (Exception ex){
				Success = false;
			}
		} catch (Exception e){
			Success = false;
		}
		return Success;
	}

}
