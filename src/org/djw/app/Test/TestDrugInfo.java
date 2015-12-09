package org.djw.app.Test;

import org.json.JSONObject;

public class TestDrugInfo {
	
	public TestDrugInfo(){}
	
	public boolean testDrugInfo(String ServiceURL,String MatchString){
		boolean Success = false;
	
		ServiceClient serviceClient = new ServiceClient();
		try{
			String TestOutput = serviceClient.getService(ServiceURL);
//			System.out.println(ServiceURL);
//			System.out.println(TestOutput);
			try{
				JSONObject json = new JSONObject(TestOutput);
				JSONObject Body = json.getJSONObject("Body");
				JSONObject DrugInfo = Body.getJSONObject("DrugInfo");
				String resultString = DrugInfo.getString("spl_set_id");
				if (resultString.equals(MatchString)){
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
