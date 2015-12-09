package org.djw.app.Test;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestSearchDrug {
	
	public TestSearchDrug(){}
	
	public boolean testDrug(String ServiceURL,String MatchString){
		boolean Success = false;
	
		ServiceClient serviceClient = new ServiceClient();
		try{
			String TestOutput = serviceClient.getService(ServiceURL);
//			System.out.println(ServiceURL);
//			System.out.println(TestOutput);
			try{
				int numMatches = 0;
				JSONObject json = new JSONObject(TestOutput);
				JSONObject Body = json.getJSONObject("Body");
				JSONObject ReportOutput = Body.getJSONObject("ReportOutput");
				JSONArray rows = ReportOutput.getJSONArray("rows");
				for (int i=0; i<rows.length(); i++){
					JSONArray row = rows.getJSONArray(i);
					String resultString = row.getString(0);
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
