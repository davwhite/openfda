package openfda.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import org.apache.log4j.Logger;
import org.djw.tools.utils.GeneralUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class DailyMedClient {
	final static Logger logger = Logger.getLogger(DailyMedClient.class);

	/**
	 * RestClient is used to consume restful services. This particular class is setup to pull from DailyMed service.
	 */
	public DailyMedClient(){}
	
	/**
	 * Gets data from a restful service using http get
	 * @param ServiceURI
	 * @return JSONObject
	 * @throws JSONException
	 * @throws IOException
	 */
	public JSONObject getService(String ServiceURI) throws JSONException, IOException{
		JSONObject replyJson = new JSONObject();
	    String ServiceURL = "";
	    if (GeneralUtils.getConfigPropValue("dailymed_service") != null){
	    	ServiceURL = GeneralUtils.getConfigPropValue("dailymed_service");
	    }
	    String myURL = ServiceURL + ServiceURI;
	    if (logger.isDebugEnabled()){
			logger.debug("Requested URL:" + myURL);
	    }
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			logger.fatal("Exception while calling URL:"+ myURL, e);
		} 
 
		try {
			String sResponse = sb.toString();
			replyJson = new JSONObject(sResponse);
		} catch (Exception ex){
			logger.fatal("unable to parse json: " + ex);
		}
		return replyJson;
	}
}
