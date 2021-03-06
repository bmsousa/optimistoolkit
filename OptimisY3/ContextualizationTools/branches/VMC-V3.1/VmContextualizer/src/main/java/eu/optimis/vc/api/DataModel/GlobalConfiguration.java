/**
 *  Copyright 2013 University of Leeds
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package eu.optimis.vc.api.DataModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Class to stores the configuration details of the VMC
 * 	
 * TODO: check for OS and dependency requirements
 * 
 * @author Django Armstrong (ULeeds)
 * @version 0.0.2
 */
public class GlobalConfiguration {
	
	protected final static Logger log = Logger
			.getLogger(GlobalConfiguration.class);
	
	private boolean defaultValues;
	private Properties properties;
	private String configFilePath;
	
	//Stored in properties file
	private String installDirectory;
	private String repository;
	
	//Generated from properties file
	private String contextDataDirectory;
	private String agentsDirectory;
	
	/**
	 * Constructor for setting configuration variables.
	 * 
 	 * @throws Exception Thrown if config file is not present or unreadable.
	 */
	public GlobalConfiguration(String configFilePath) throws Exception {
		
		this.configFilePath = configFilePath;
		log.info("Using configFilePath: '"+configFilePath+"'");
		defaultValues = false;
		
		config();
	}
	
	/**
	 * Constructor used for testing purposes, using default configuration values
	 * 
	 */
	public GlobalConfiguration() {
		
		log.info("Using default config values for testing purposes...");
		defaultValues = true;
		
		try {
			config();
		} catch (Exception e) {
			//Do nothing as config file is not loaded here...
		}
	}
	
	/**
	 * Gets the variables from the config properties file.
	 * 
	 * @throws Exception Thrown if config file is not present or unreadable.
	 */
	private void config() throws Exception{
		
		// Read properties file.
		this.properties = new Properties();
		
		if (defaultValues == true) {
			//TODO: Changes these paths to OS specific temp folders
			properties.setProperty("installDirectory", "C:/VmContextualizer/runtime");
			properties.setProperty("repository", "C:/VmContextualizer/repository");
		} else {
			try {
			    properties.load(new FileInputStream(this.configFilePath + "/config.properties"));
			} catch (IOException e) {
				log.error("The config.properties file does not exist at location: " + this.configFilePath + "/config.properties", e);
				throw new Exception("The config.properties file does not exist!", e);
			}	
		}

	    this.installDirectory = new String();
		installDirectory = properties.getProperty("installDirectory");
		log.info("Using install dir: '" + installDirectory + "'");
		
		this.repository = new String();
		repository = properties.getProperty("repository");
		log.info("Using repository dir: '" + repository + "'");
		
		this.contextDataDirectory = new String();
		contextDataDirectory = installDirectory + "/context";
		
		this.agentsDirectory = new String();
		agentsDirectory = installDirectory + "/agents";
	}
	
	/**
	 * @return the installDirectory
	 */
	public String getInstallDirectory() {
		return installDirectory;
	}

	/**
	 * @param installDirectory the installDirectory to set
	 */
	public void setInstallDirectory(String installDirectory) {
		this.installDirectory = installDirectory;
	}

	/**
	 * @return the repository
	 */
	public String getRepository() {
		return repository;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(String repository) {
		this.repository = repository;
	}

	/**
	 * @return the contextDataDirectory
	 */
	public String getContextDataDirectory() {
		return contextDataDirectory;
	}

	/**
	 * @param contextDataDirectory the contextDataDirectory to set
	 */
	public void setContextDataDirectory(String contextDataDirectory) {
		this.contextDataDirectory = contextDataDirectory;
	}

	/**
	 * @return the agentsDirectory
	 */
	public String getAgentsDirectory() {
		return agentsDirectory;
	}

	/**
	 * @param agentsDirectory the agentsDirectory to set
	 */
	public void setAgentsDirectory(String agentsDirectory) {
		this.agentsDirectory = agentsDirectory;
	}

	/**
	 * @return the defaultValues
	 */
	public boolean isDefaultValues() {
		return defaultValues;
	}

	/**
	 * @return the configFilePath
	 */
	public String getConfigFilePath() {
		return configFilePath;
	}
}
