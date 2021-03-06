/**
 * Copyright (C) 2010-2013 Barcelona Supercomputing Center
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with this library; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package eu.optimis.ip.gui.client.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 *
 * @author jsubirat
 */
public class ConfigManager {

    private static Logger log = Logger.getLogger(ConfigManager.class);
    private static String optimis_Home = System.getenv("OPTIMIS_HOME");
    public static final String IPMANAGER_CONFIG_FILE = "/etc/IPManagerWeb/config.properties";
    public static final String DATABASE_CONFIG_FILE = "/etc/IPManagerWeb/database/database.properties";
    public static final String LOG4J_CONFIG_FILE = "/etc/IPManagerWeb/log4jIPMW.properties";
    public static final String COMPONENT_CONFIGURATION_FOLDER = "/etc";
    public static final String COMPONENT_LOGGING_FOLDER = "/var/log";
    
    public static PropertiesConfiguration getPropertiesConfiguration(String configFile) {
        String filePath = null;
        PropertiesConfiguration config = null;

        try {
            filePath = getFilePath(configFile);
            config = new PropertiesConfiguration(filePath);
        } catch (ConfigurationException ex) {
            log.error("Error reading " + filePath + " configuration file: " + ex.getMessage());
            log.error(ex.getMessage());
        }

        return config;
    }

    public static String getFilePath(String configFile) {
        String optimisHome = optimis_Home;
        if (optimisHome == null) {
            optimis_Home = System.getenv("OPTIMIS_HOME");
            optimisHome = "/opt/optimis";
            log.warn("Please set environment variable OPTIMIS_HOME. Using default /opt/optimis.");
        }

        File fileObject = new File(optimisHome.concat(configFile));
        if (!fileObject.exists()) {
            try {
                createDefaultConfigFile(fileObject);
            } catch (Exception ex) {
                log.error("Error reading " + optimisHome.concat(configFile) + " configuration file: " + ex.getMessage());
                log.error(ex.getMessage());
            }
        }

        return optimisHome.concat(configFile);
    }

    private static void createDefaultConfigFile(File fileObject) throws Exception {
        log.debug("File " + fileObject.getAbsolutePath() + " didn't exist. Creating one with default values...");

        //Create parent directories.
        log.debug("Creating parent directories.");
        new File(fileObject.getParent()).mkdirs();

        //Create an empty file to copy the contents of the default file.
        log.debug("Creating empty file.");
        new File(fileObject.getAbsolutePath()).createNewFile();

        //Copy file.
        log.debug("Copying file " + fileObject.getName());
        InputStream streamIn = ConfigManager.class.getResourceAsStream("/" + fileObject.getName());
        FileOutputStream streamOut = new FileOutputStream(fileObject.getAbsolutePath());
        byte[] buf = new byte[8192];
        while (true) {
            int length = streamIn.read(buf);
            if (length < 0) {
                break;
            }
            streamOut.write(buf, 0, length);
        }

        //Close streams after copying.
        try {
            streamIn.close();
        } catch (IOException ex) {
            log.error("Couldn't close input stream");
            log.error(ex.getMessage());
        }
        try {
            streamOut.close();
        } catch (IOException ex) {
            log.error("Couldn't close file output stream");
            log.error(ex.getMessage());
        }
    }
}
