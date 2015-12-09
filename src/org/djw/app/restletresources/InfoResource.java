package org.djw.app.restletresources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.djw.tools.json.ResponseJson;
import org.djw.tools.utils.DjwToolsInfo;
import org.djw.tools.utils.GeneralUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class InfoResource extends ServerResource {
	final static Logger logger = Logger.getLogger(InfoResource.class);
	
	/**
	 * Returns version information about the application service and libraries  
	 * @return JSONObject
	 * @throws JSONException
	 * 		
	 */
	@Get
	public Representation represent() throws JSONException {
		String ServiceURL = "";
		if (GeneralUtils.getConfigPropValue("service_url") != null){
			ServiceURL = GeneralUtils.getConfigPropValue("service_url");
	    }
		String absolute_base_path = "";
		if (GeneralUtils.getConfigPropValue("absolute_base_path") != null){
			absolute_base_path = GeneralUtils.getConfigPropValue("absolute_base_path");
	    }
		String VersionFile = "";
		if (GeneralUtils.getConfigPropValue("version_file") != null){
			VersionFile = GeneralUtils.getConfigPropValue("version_file");
	    }
		String BuildFile = "";
		if (GeneralUtils.getConfigPropValue("build_file") != null){
			BuildFile = GeneralUtils.getConfigPropValue("build_file");
	    }

		VersionFile = absolute_base_path + VersionFile;
		BuildFile = absolute_base_path + BuildFile;
		
		String Version = getFile(VersionFile);
		String Build = getFile(BuildFile);
		
		DjwToolsInfo djwTools = new DjwToolsInfo();
		JSONObject djwInfo = djwTools.getInfo();
		
		
		JSONObject jBody = new JSONObject();
		
		jBody.put("ServiceURL", ServiceURL);
		jBody.put("Version", Version);
		jBody.put("Build", Build);
		jBody.put("djwToolsInfo", djwInfo);

		ResponseJson jResponse = new ResponseJson();
		Representation rep = null;
		jResponse.setStatusCode(0);
		jResponse.setStatusMessage("");
		jResponse.setBody(jBody);
		rep = new JsonRepresentation(jResponse.getResponse(jResponse));
		return rep;
	}
	
	/**
	 * Returns application version. Version is stored in a config file which is incremented through CI  
	 * @param String VersionFile File containing current version number
	 * @return String
	 * 		
	 */
	private String getFile(String FileName){
		String version = "";
		BufferedReader br = null;
		 
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(FileName));
 
			while ((sCurrentLine = br.readLine()) != null) {
				version = sCurrentLine;
			}
 
		} catch (IOException e) {
			logger.fatal(e);
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				logger.fatal(ex);
			}
		}
		return version;
	}
}

