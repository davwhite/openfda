package org.djw.app.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Config {
	public Config(){}
	
	public String getConfigValue(String ConfigFile, String ConfigProperty){
		String configValue = "";
		try (BufferedReader br = new BufferedReader(new FileReader(ConfigFile)))
		{
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				int eq = sCurrentLine.indexOf("=");
				String param = sCurrentLine.substring(0, eq);
				
				if (param.equals(ConfigProperty)){
					configValue = sCurrentLine.substring(eq + 1, sCurrentLine.length());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return configValue;
	}

}
