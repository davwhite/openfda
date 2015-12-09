package org.djw.app.Test;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestChart {
	
	public TestChart(){}
	
	public boolean testChart(String ServiceURL){
		boolean Success = false;
	
		ServiceClient serviceClient = new ServiceClient();
		try{
			String TestOutput = serviceClient.getService(ServiceURL);
//			System.out.println(ServiceURL);
//			System.out.println(TestOutput);
			try{
				JSONObject json = new JSONObject(TestOutput);
				JSONObject Body = json.getJSONObject("Body");
				JSONArray chartdata = Body.getJSONArray("chartdata");
				
				//get the first record. it MUST be the headers
				JSONArray headerRecord = chartdata.getJSONArray(0);
				if (headerRecord.getString(0).equals("Time") && headerRecord.getString(1).equals("Count")){
					// all data must be numbers
					for (int i=1; i<chartdata.length(); i++){
						JSONArray data = chartdata.getJSONArray(i);
						try{
							@SuppressWarnings("unused")
							int n1 = data.getInt(0);
							@SuppressWarnings("unused")
							int n2 = data.getInt(1);
							Success = true;
						} catch (Exception en){
							Success = false;
						}
					}
				} else {
					Success = false;
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
