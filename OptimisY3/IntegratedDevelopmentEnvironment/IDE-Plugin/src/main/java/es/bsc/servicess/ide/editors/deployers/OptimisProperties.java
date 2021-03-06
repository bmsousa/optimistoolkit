/*
 *  Copyright 2011-2012 Barcelona Supercomputing Center (www.bsc.es)
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

package es.bsc.servicess.ide.editors.deployers;

import integratedtoolkit.ITConstants;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class OptimisProperties {
	private static final String ICS_LOC = "ics.location";
	private static final String DS_LOC = "ds.location";
	private static final String SM_LOC = "sm.location";
	private static final String LS_LOC = "ls.location";
	private static final String QUOTA_FACTOR = "quota.factor";
	private static final String TOLERANCE_FACTOR = "tolerance.factor";
	private static final String LS_CLI_PROP_LOCATION = "ls.client.properties";

	private PropertiesConfiguration config;

	public OptimisProperties(String pathToConfigFile)
			throws ConfigurationException {
		config = new PropertiesConfiguration(pathToConfigFile);
	}

	public OptimisProperties(File file) throws ConfigurationException {
		config = new PropertiesConfiguration(file);
	}

	public String getICSLocation() {
		return config.getString(ICS_LOC, "");
	}

	public void setICSLocation(String location) {
		config.setProperty(ICS_LOC, location);
	}

	public String getDSLocation() {
		return config.getString(DS_LOC, "");
	}

	public void setDSLocation(String location) {
		config.setProperty(DS_LOC, location);
	}

	public String getSMLocation() {
		return config.getString(SM_LOC, null);
	}

	public void setSMLocation(String location) {
		config.setProperty(SM_LOC, location);
	}

	public float getToleranceFactor() {
		return config.getFloat(TOLERANCE_FACTOR, 0.9f);
	}

	public float getQuotaFactor() {
		return config.getFloat(QUOTA_FACTOR, 1.3f);
	}

	public void save() throws ConfigurationException {
		config.save();
	}
	
	public String getLSLocation() {
		return config.getString(LS_LOC, "");
	}
	
	public void setLSLocation(String location) {
		config.setProperty(LS_LOC, location);		
	}

	public String getLSClientProperties() {
		return config.getString(LS_CLI_PROP_LOCATION, "");
	}
	
	public void setLSClientProperties(String location) {
		config.setProperty(LS_CLI_PROP_LOCATION, location);
		
	}
}
