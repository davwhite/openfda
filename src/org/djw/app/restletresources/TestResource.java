package org.djw.app.restletresources;

import org.apache.log4j.Logger;
import org.djw.tools.json.ErrorJson;
import org.djw.tools.json.ResponseJson;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class TestResource extends ServerResource {
	final static Logger logger = Logger.getLogger(TestResource.class);

	/**
	 * A simple test service used for configuration and testing of the service platform. This function handles get requests.   
	 * @return JSONObject
	 * @throws JSONException
	 * 		
	 */
	@Get
	public Representation represent() throws JSONException {
		Representation rep = null;
		String Error = "";
		if (getRequest().getAttributes().get("Error") != null ) Error = (String) getRequest().getAttributes().get("Error");
		ResponseJson jResponse = new ResponseJson();
		JSONObject jTestResponse = new JSONObject();
		if (Error.equals("")){
			jTestResponse.put("Message", "This is a test message");
			jResponse.setStatusCode(0);
			jResponse.setStatusMessage("");
			jResponse.setBody(jTestResponse);
		} else {
			ErrorJson errorJson = new ErrorJson();
			int errorCode = 200;
			String errorMessage = "This is a test error message";
			JSONObject jError = errorJson.make(errorCode, errorMessage);
			jTestResponse.put("ErrorCode", "11111");
			jTestResponse.put("ErrorMessage", "This is an error message");
			jResponse.setStatusCode(1);
			jResponse.setStatusMessage("This is an error status");
			jResponse.setBody(jError);
		}
		rep = new JsonRepresentation(jResponse.getResponse(jResponse));
		return rep;
	}
	
	/**
	 * A simple test service used for configuration and testing of the service platform. This function handles post requests.   
	 * @return JSONObject
	 * @throws JSONException
	 * 		
	 */
	@Post("json:json")
	public Representation acceptJson(JsonRepresentation represent) throws ResourceException {
		Representation rep = null;
		try {
			JSONObject jsonobject = represent.getJsonObject();
			String requestString = jsonobject.getString("request");
			JSONObject json = new JSONObject(requestString);
			getResponse().setStatus(Status.SUCCESS_ACCEPTED);
			JSONStringer jsReply = new JSONStringer();
			jsReply.object();
			jsReply.key("PostedFields").value(json);
			jsReply.endObject();
			JSONObject jReply = new JSONObject(jsReply.toString());
			ResponseJson responseJson = new ResponseJson();
			responseJson.setStatusCode(0);
			responseJson.setStatusMessage("");
			responseJson.setBody(jReply);
			JSONObject jResponse = responseJson.getResponse(responseJson);
			rep = new JsonRepresentation(jResponse);
			getResponse().setStatus(Status.SUCCESS_OK);
		} catch (Exception e) {
			logger.fatal(e);
			JSONStringer jsReply = new JSONStringer();
			try {
				jsReply.object();
				jsReply.key("CODE").value("Error");
				jsReply.key("DESC").value(e.getMessage());
				jsReply.endObject();
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
		}
		return rep;
	}
}