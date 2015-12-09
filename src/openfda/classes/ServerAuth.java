package openfda.classes;

import org.djw.tools.utils.GeneralUtils;

public class ServerAuth {

	public ServerAuth(){}
	
	/**
	 * Authenticates service requests. Servers with the correct authentication token are allowed to use the service.
	 * Current build uses a stored key distributed to each server. If the key is correct true is returned.
	 * 
	 * 		
	 * @param String ServerKey
	 * @return boolean
	 */
	public boolean authenticate(String ServerKey){
		boolean Success = false;
		String StoredKey = "";
		if (GeneralUtils.getConfigPropValue("server_key") != null){
			StoredKey = GeneralUtils.getConfigPropValue("server_key");
	    }
		if (StoredKey.equals(ServerKey)){
			Success = true;
		} else {
			Success = false;
		}
		return Success;
	}
	
	/**
	 * getKey reads in the server key from the configuration file. 
	 * 
	 * 		
	 * @return String
	 */
	public String getKey(){
		String StoredKey = "";
		if (GeneralUtils.getConfigPropValue("server_key") != null){
			StoredKey = GeneralUtils.getConfigPropValue("server_key");
	    }
		return StoredKey;
	}
}
